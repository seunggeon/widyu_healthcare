package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.FamilyIdxResponse;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserDetail;
import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.db.mapper.v1.GuardiansMapper;
import com.widyu.healthcare.support.error.exception.DuplicateIdException;
import com.widyu.healthcare.support.error.exception.MissingFileException;
import com.widyu.healthcare.support.error.exception.NoDataException;
import com.widyu.healthcare.support.utils.SHA256Util;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class GuardiansService {
    private final GuardiansMapper guardiansMapper;
    private final SeniorsMapper seniorsMapper;
    private final S3Service s3Service;
    @Transactional(rollbackFor = RuntimeException.class)
    public GuardianInfoResponse insert(User EncryptedUser) {
        // 유저 아이디 중복 체크
        boolean duplicatedIdResult = isDuplicatedId(EncryptedUser.getId());
        if (duplicatedIdResult) {
            throw new DuplicateIdException("이미 가입한 아이디 입니다.");
        }

        // 유저 상세 정보 저장
        long insertCount = guardiansMapper.insertDetail(EncryptedUser);
        if (insertCount != 1) {
            log.error("guardiansMapper.insertDetail method ERROR! Guardian ID : ", EncryptedUser.getId());
            throw new RuntimeException(
                    "insert Guardiance detail info ERROR! 회원가입 메서드를 확인해주세요\n" + "Guardian ID : " + EncryptedUser.getId());
        }

        // 유저 이름 저장
        int insertNameCount = guardiansMapper.update(EncryptedUser.getUserIdx(), EncryptedUser.getName());
        if (insertNameCount != 1) {
            log.error("guardiansMapper.update method ERROR! Guardian ID : ", EncryptedUser.getId());
            throw new RuntimeException(
                    "update Guardiance info ERROR! 회원가입 메서드를 확인해주세요\n" + "Guardian ID : " + EncryptedUser.getId());
        }
        return GuardianInfoResponse
                .builder()
                .userIdx(EncryptedUser.getUserIdx())
                .name(EncryptedUser.getName())
                .id(EncryptedUser.getId())
                .phoneNumber(EncryptedUser.getPhoneNumber())
                .build();
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonUserResponse loginByIdAndPassword(UserDetail loginInfo, HttpSession session) {
        CommonUserResponse userInfo = guardiansMapper.findByIdAndPassword(loginInfo.getId(), loginInfo.getPassword());
        if(userInfo == null){
            log.error("guardiansMapper.findByIdAndPassword method ERROR! Guardian ID : ", loginInfo.getId());
            throw new NoDataException("login Guardian ERROR! 가입된 회원 정보가 없습니다.");
        }

        int updateCount = guardiansMapper.updateFCM(userInfo.getUserIdx(), loginInfo.getFcmToken());
        if(updateCount == 0){
            log.error("guardiansMapper.updateFCM method ERROR! Guardian ID : ", loginInfo.getId());
            throw new RuntimeException("login Guardian ERROR! FCM 저장에 실패했습니다.\n" + "Guardian ID : " + loginInfo.getId());
        }

        if (UserStatus.ACTIVE.equals(userInfo.getStatus())) {
            SessionUtil.setLoginGuardianIdx(session, userInfo.getUserIdx());
            log.info("Guardian session 저장 성공", userInfo.getUserIdx());
        }

        return userInfo;
    }

    public GuardianInfoResponse findId(String name, String phoneNumber){
        GuardianInfoResponse userInfo = guardiansMapper.findIdByNameAndNumber(name, phoneNumber);
        if(userInfo == null){
            log.error("guardiansMapper.findIdByNameAndNumber ERROR! name : ", name);
            throw new NoDataException("아이디 찾기 Guardian ERROR! 가입된 회원 정보가 없습니다.\n");
        }

        return userInfo;
    }

    public void findPassword(String id, String newPassword, String name, String phoneNumber){
        int updateCount = guardiansMapper.updatePassword(id, SHA256Util.encryptSHA256(newPassword), name, phoneNumber);
        if(updateCount == 0){
            log.error("guardiansMapper.updatePassword ERROR! name : ", name);
            throw new NoDataException("비밀번호 찾기 Guardian ERROR! 가입된 회원 정보가 없습니다.\n");
        }
    }

    public boolean isDuplicatedId(String id) {
        return guardiansMapper.checkId(id) == 1;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public FamilyInfoResponse getSeniorsAndTargetInfo(long userIdx){
        GuardianInfoResponse targetInfo = guardiansMapper.findByIdx(userIdx);
        List<SeniorInfoResponse> seniorDetailList = guardiansMapper.findSeniorsByIdx(userIdx);
        List<GuardianInfoResponse> guardianDetailList = seniorsMapper.findGuardiansByIdx(userIdx);

        FamilyInfoResponse familyInfo = FamilyInfoResponse.builder()
                .targetInfoResponse(targetInfo)
                .guardianInfoResponseList(guardianDetailList)
                .seniorInfoResponseList(seniorDetailList)
                .build();
        return familyInfo;
    }

    public FamilyIdxResponse getFamilyIdxOfTarget(long userIdx){
        List<Long> seniorsIdxList = guardiansMapper.findSeniorsIdxByIdx(userIdx);
        List<Long> guardiansIdxList = guardiansMapper.findGuardiansIdxByIdx(userIdx);
        // null 값 제거
        seniorsIdxList.removeAll(Collections.singletonList(null));
        guardiansIdxList.removeAll(Collections.singletonList(null));
        return FamilyIdxResponse.builder()
                .seniorsIdxList(seniorsIdxList)
                .guardiansIdxList(guardiansIdxList)
                .build();
    }

    public void addGuardian(long guardianIdx, long targetIdx) {
        int relationInsertCount = guardiansMapper.insertRelationWithGuardian(guardianIdx, targetIdx);
        if(relationInsertCount != 1) {
            log.error("guardiansMapper.insertRelationWithGuardian method ERROR! Guardian Idx : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 보호자 추가 등록 메서드를 확인해주세요\n" + "Guardian Idx : " + guardianIdx);
        }
    }

    public void updateProfile(long userIdx, User user) throws IOException {
        int updateCount = guardiansMapper.updateProfile(userIdx, user.getName(), user.getPhoneNumber(), user.getAddress(), user.getBirth());
        if(updateCount == 0){
            log.error("guardiansMapper.updateProfile method ERROR! Guardian Idx : ", userIdx);
            throw new RuntimeException("update Guardian profile ERROR! 보호자 프로필 수정 메서드를 확인해주세요\n" + "Guardian Idx : : " + userIdx);
        }
    }

    public void updateProfileImage(long userIdx, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new MissingFileException("업로드할 file이 없습니다.");
        String profileImageUrl = s3Service.upload(multipartFile);
        int updateCount = guardiansMapper.updateProfileImage(userIdx, profileImageUrl);
        if(updateCount == 0){
            log.error("guardiansMapper.updateProfileImage method ERROR! Guardian Idx : ", userIdx);
            throw new RuntimeException("update Guardian profile image ERROR! 보호자 프로필 이미지 수정 메서드를 확인해주세요\n" + "Guardian Idx : " + userIdx);
        }
    }
}

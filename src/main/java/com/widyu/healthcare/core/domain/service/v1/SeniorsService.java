package com.widyu.healthcare.core.domain.service.v1;

import java.io.IOException;
import java.util.List;

import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.db.mapper.v1.HealthsMapper;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.support.error.exception.MissingFileException;
import com.widyu.healthcare.support.error.exception.NoDataException;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeniorsService {
    private final SeniorsMapper seniorsMapper;
    private final HealthsMapper healthsMapper;
    private final S3Service s3Service;
    @Transactional(rollbackFor = RuntimeException.class)
    public String insertAndSetRelations(long guardianIdx, User EncryptedUser) {

        // 유저 상세 정보 저장
        int insertCount = seniorsMapper.insertDetail(EncryptedUser);
        if (insertCount != 1) {
            log.error("seniorsMapper.insertDetail method ERROR! GuardianIdx of senior : ", guardianIdx);
            throw new RuntimeException(
                    "insert Senior 상세 정보 ERROR! 회원가입 메서드를 확인해주세요\n" + "GuardianIdx of senior : " + guardianIdx);
        }

        // 유저 개요 정보 저장
        int insertNameCount = seniorsMapper.update(EncryptedUser.getUserIdx(), EncryptedUser.getName(), EncryptedUser.getType());
        if (insertNameCount != 1) {
            log.error("seniorsMapper.update method ERROR! GuardianIdx of senior : ", guardianIdx);
            throw new RuntimeException(
                    "insert Senior 개요 정보 ERROR! 회원가입 메서드를 확인해주세요\n" + "GuardianIdx of senior : " + guardianIdx);
        }

        // Diseases 테이블 저장
        if(Boolean.TRUE.equals(EncryptedUser.getIsDisease()))
        {
            int insertDiseasesCount = seniorsMapper.insertDiseases(EncryptedUser.getUserIdx(), EncryptedUser.getDiseases());
            if(insertDiseasesCount == 0) {
                log.error("seniorsMapper.insertDiseases method ERROR! GuardianIdx of senior : ", guardianIdx);
                throw new RuntimeException(
                        "insert Disease during register ERROR! 질병 추가에 실패했습니다\n" + "GuardianIdx of senior : " + guardianIdx);
            }
        }

        // relation(가족 관계) 테이블 저장
        int relationInsertCount = seniorsMapper.insertRelationWithSenior(guardianIdx, EncryptedUser.getUserIdx());
        if(relationInsertCount != 1) {
            log.error("seniorsMapper.insertRelationWithSenior method ERROR! GuardianIdx of senior : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 가족 관계 설정에 실패했습니다\n" + "GuardianIdx of senior : " + guardianIdx);
        }

        // 위치 테이블 기본값 저장
        int locationInsertCount = healthsMapper.insertDefaultLocation(EncryptedUser.getUserIdx());
        if(locationInsertCount != 1) {
            log.error("healthsMapper.insertDefaultLocation method ERROR! GuardianIdx of senior : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Location during register ERROR! 위치 정보 저장에 실패했습니다\n" + "GuardianIdx of senior : " + guardianIdx);
        }

        return EncryptedUser.getInviteCode();
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonUserResponse loginByInviteCode(String inviteCode, String fcmToken, HttpSession session) {
        CommonUserResponse userInfo = seniorsMapper.findByInviteCode(inviteCode);
        if(userInfo == null){
            log.error("seniorsMapper.findByInviteCode method ERROR! inviteCode : ", inviteCode);
            throw new NoDataException("login Senior ERROR! 회원정보가 없습니다.\n" + "inviteCode : " + inviteCode);
        }

        int updateCount = seniorsMapper.updateFCM(userInfo.getUserIdx(), fcmToken);
        if(updateCount == 0){
            log.error("seniorsMapper.updateFCM method ERROR! inviteCode : ", inviteCode);
            throw new RuntimeException("login Senior ERROR! FCM 저장에 실패했습니다.\n" + "inviteCode : " + inviteCode);
        }

        if (UserStatus.ACTIVE.equals(userInfo.getStatus())) {
            SessionUtil.setLoginSeniorIdx(session, userInfo.getUserIdx());
            log.info("Senior session 저장 성공", userInfo.getUserIdx());
        }

        return userInfo;
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public FamilyInfoResponse getGuardiansAndTargetInfo(long userIdx){
        SeniorInfoResponse targetInfo = seniorsMapper.findDetailByIdx(userIdx);
        List<GuardianInfoResponse> guardianInfoList = seniorsMapper.findGuardiansByIdx(userIdx);

        FamilyInfoResponse familyInfo = FamilyInfoResponse.builder()
                .targetInfoResponse(targetInfo)
                .guardianInfoResponseList(guardianInfoList)
                .build();
        return familyInfo;
    }

    public GuardianInfoResponse findGuardianByGuardianId(String id) {
        GuardianInfoResponse guardians = seniorsMapper.findGuardianByGuardianId(id);

        return guardians;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void updateProfile(long userIdx, User user) throws IOException {
        int updateCount = seniorsMapper.updateProfile(userIdx, user.getName(), user.getBirth(), user.getPhoneNumber(), user.getAddress(), user.getIsDisease());
        if(updateCount == 0){
            log.error("seniorsMapper.updateProfile method ERROR! userIdx : ", userIdx);
            throw new RuntimeException("update senior profile ERROR! \n" + " userIdx : " + userIdx);
        }
        user.getDiseases().forEach(disease -> seniorsMapper.updateDisease(userIdx, disease));
    }

    public void updateProfileImage(long userIdx, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new MissingFileException("업로드할 file이 없습니다.");
        String profileImageUrl = s3Service.upload(multipartFile);
        int updateCount = seniorsMapper.updateProfileImage(userIdx, profileImageUrl);
        if(updateCount == 0){
            log.error("seniorsMapper.updateProfileImage method ERROR! userIdx : ", userIdx);
            throw new RuntimeException("update senior profile image ERROR! \n" + "userIdx : " + userIdx);
        }
    }

    public void insertDisease(long userIdx, Disease disease) {
        int insertCount = seniorsMapper.insertDisease(userIdx, disease);
        if(insertCount == 0){
            log.error("seniorsMapper.updateProfileImage method ERROR! userIdx : ", userIdx);
            throw new RuntimeException("insert senior disease ERROR! \n" + " userIdx : " + userIdx);
        }
    }

    public void deleteDisease(long userIdx, Long diseaseIdx) {
        seniorsMapper.deleteDisease(userIdx, diseaseIdx);
    }
}

package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.request.guardian.UpdateGuardianProfileRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.RegisterGuardianRequest;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.support.error.exception.DuplicateIdException;
import com.widyu.healthcare.core.db.mapper.v1.GuardiansMapper;
import com.widyu.healthcare.support.utils.SHA256Util;
import com.widyu.healthcare.support.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class GuardiansService {
    private final GuardiansMapper guardiansMapper;
    @Transactional(rollbackFor = RuntimeException.class)
    public GuardianInfoResponse insert(User EncryptedUser) {
        boolean duplIdResult = isDuplicatedId(EncryptedUser.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }

        long insertCount = guardiansMapper.insertDetail(EncryptedUser);
        if (insertCount != 1) {
            log.error("insert Guardiance ERROR! {}", EncryptedUser);
            throw new RuntimeException(
                    "insert Guardiance ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + EncryptedUser);
        }
        int insertNameCount = guardiansMapper.update(EncryptedUser.getUserIdx(), EncryptedUser.getName());
        if (insertNameCount != 1) {
            log.error("update Guardiance info ERROR! info from user table is null {}", EncryptedUser);
            throw new RuntimeException(
                    "update Guardiance info ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + EncryptedUser);
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
    public CommonUserResponse loginByIdAndPassword(String id, String cryptoPassword, String fcmToken, HttpSession session) {
        CommonUserResponse userInfo = guardiansMapper.findByIdAndPassword(id, cryptoPassword);
        if(userInfo == null){
            throw new DuplicateIdException("login Guardian ERROR! 회원정보가 없습니다.\n");
        }

        int updateCount = guardiansMapper.updateFCM(userInfo.getUserIdx(), fcmToken);
        if(updateCount == 0){
            log.error("login Senior ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("login Senior ERROR! FCM 저장에 실패했습니다.\n" + "FCM Token : " + fcmToken);
        }

        if (UserStatus.ACTIVE.equals(userInfo.getStatus())) {
            SessionUtil.setLoginGuardianIdx(session, userInfo.getUserIdx());
            log.info("session 저장 성공", userInfo.getUserIdx());
        }

        return userInfo;
    }

    public GuardianInfoResponse findId(String name, String phoneNumber){
        GuardianInfoResponse userInfo = guardiansMapper.findIdByNameAndNumber(name, phoneNumber);
        if(userInfo == null){
            throw new DuplicateIdException("아이디 찾기 Guardian ERROR! 회원정보가 없습니다.\n");
        }

        return userInfo;
    }

    public GuardianInfoResponse findPassword(String id, String newPassword, String name, String phoneNumber){
        GuardianInfoResponse userInfo = guardiansMapper.updatePasswordByGuardianInfos(id, SHA256Util.encryptSHA256(newPassword), name, phoneNumber);
        if(userInfo == null){
            throw new DuplicateIdException("비밀번호 찾기 Guardian ERROR! 회원정보가 없습니다.\n");
        }
        return userInfo;
    }

    public boolean isDuplicatedId(String id) {
        return guardiansMapper.checkId(id) == 1;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public FamilyInfoResponse getSeniorsAndMyInfo(long userIdx){
        GuardianInfoResponse myInfo = guardiansMapper.findByIdx(userIdx);
        List<SeniorInfoResponse> seniorDetailList = guardiansMapper.findSeniorsByIdx(userIdx);

        FamilyInfoResponse familyInfo = FamilyInfoResponse.builder()
                .guardianInfoResponseList(myInfo.toList())
                .seniorInfoResponseList(seniorDetailList)
                .build();
        return familyInfo;
    }

    public void updateProfile(long userIdx, User user){
        int updateCount = guardiansMapper.updateProfile(userIdx, user.getName(), user.getProfileImageUrl(), user.getPhoneNumber(), user.getAddress(), user.getBirth());
        if(updateCount == 0){
            log.error("update Guardian profile ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("update Guardian profile ERROR! \n" + "update : " + user);
        }
    }
}

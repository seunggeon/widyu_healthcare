package com.widyu.healthcare.core.domain.service.v1;

import java.util.List;

import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.support.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.widyu.healthcare.support.error.exception.DuplicateIdException;
import org.springframework.transaction.annotation.Transactional;

import static com.widyu.healthcare.core.domain.domain.v1.UserType.SENIOR;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeniorsService {
    private SeniorsMapper seniorsMapper;
    @Transactional(rollbackFor = RuntimeException.class)
    public String insertAndSetRelations(long guardianIdx, User EncryptedUser) {

        // UserDetail 테이블 저장
        int insertCount = seniorsMapper.insertDetail(EncryptedUser);
        if (insertCount != 1) {
            log.error("insert Senior ERROR! info from userdetail table is null {}", EncryptedUser);
            throw new RuntimeException(
                    "insert Senior ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + EncryptedUser);
        }

        // User 테이블 저장
        int insertNameCount = seniorsMapper.update(EncryptedUser.getUserIdx(), EncryptedUser.getName(), EncryptedUser.getType());
        if (insertNameCount != 1) {
            log.error("update Senior info ERROR! info from user table is null {}", EncryptedUser);
            throw new RuntimeException(
                    "update Senior info ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + EncryptedUser);
        }

        // Diseases 테이블 저장
        if(Boolean.TRUE.equals(EncryptedUser.getIsDisease()))
        {
            int insertDiseasesCount = seniorsMapper.insertDiseases(EncryptedUser.getUserIdx(), EncryptedUser.getDiseases());
            if(insertDiseasesCount == 0) {
                log.error("insert Disease during register ERROR! guadianIdx : ", guardianIdx);
                throw new RuntimeException(
                        "insert Disease during register ERROR! 회원가입 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx + "seniorIdx : " + EncryptedUser.getUserIdx());
            }
        }

        // relation(가족 관계) 테이블 저장
        int relationInsertCount = seniorsMapper.insertRelationWithSenior(guardianIdx, EncryptedUser.getUserIdx());
        if(relationInsertCount != 1) {
            log.error("set Senior Relation during register ERROR! guadianIdx : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 회원가입 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx + "seniorIdx : " + EncryptedUser.getUserIdx());
        }

        return EncryptedUser.getInviteCode();
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonUserResponse loginByInviteCode(String inviteCode, String fcmToken, HttpSession session) {
        CommonUserResponse userInfo = seniorsMapper.findByInviteCode(inviteCode);
        if(userInfo == null){
            log.error("login Senior ERROR! userinfo is null {}", inviteCode);
            throw new DuplicateIdException("login Senior ERROR! 회원정보가 없습니다.\n" + "Params : " + inviteCode);
        }

        int updateCount = seniorsMapper.updateFCM(userInfo.getUserIdx(), fcmToken);
        if(updateCount == 0){
            log.error("login Senior ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("login Senior ERROR! FCM 저장에 실패했습니다.\n" + "FCM Token : " + fcmToken);
        }

        if (UserStatus.ACTIVE.equals(userInfo.getStatus())) {
            SessionUtil.setLoginSeniorIdx(session, userInfo.getUserIdx());
            log.info("session 저장 성공", userInfo.getUserIdx());
        }

        return userInfo;
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public FamilyInfoResponse getGuardiansAndTargetInfo(long userIdx){
        List<GuardianInfoResponse> guardianInfoList = seniorsMapper.findGuardiansByIdx(userIdx);
        SeniorInfoResponse targetInfo = seniorsMapper.findByIdx(userIdx);

        FamilyInfoResponse familyInfo = FamilyInfoResponse.builder()
                .guardianInfoResponseList(guardianInfoList)
                .seniorInfoResponseList(targetInfo.toList())
                .build();
        return familyInfo;
    }

    public GuardianInfoResponse findGuardianByGuardianId(String id) {
        GuardianInfoResponse guardians = seniorsMapper.findGuardianByGuardianId(id);

        return guardians;
    }

    public void addGuardian(long guardianIdx, long seniorIdx) {
        int relationInsertCount = seniorsMapper.insertRelationWithSenior(guardianIdx, seniorIdx);
        if(relationInsertCount != 1) {
            log.error("set Senior Relation during register ERROR! guadianIdx : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 보호자 추가 등록 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void updateProfile(long userIdx, User user){
        int updateCount = seniorsMapper.updateProfile(userIdx, user.getName(), user.getProfileImageUrl(), user.getBirth(), user.getPhoneNumber(), user.getAddress(), user.getIsDisease());
        if(updateCount == 0){
            log.error("update senior profile ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("update senior profile ERROR! \n" + "update userIdx : " + userIdx);
        }
        user.getDiseases().stream().forEach(disease -> seniorsMapper.updateDisease(userIdx, disease));
    }
}

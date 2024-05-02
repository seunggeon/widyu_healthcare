package com.widyu.healthcare.service;

import java.util.List;
import java.util.UUID;

import com.widyu.healthcare.dto.request.SeniorProfileRequestDto;
import com.widyu.healthcare.dto.request.SeniorRequestDto;
import com.widyu.healthcare.dto.response.*;
import com.widyu.healthcare.mapper.SeniorsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.widyu.healthcare.error.exception.DuplicateIdException;
import org.springframework.transaction.annotation.Transactional;

import static com.widyu.healthcare.dto.UserType.SENIOR;

@Log4j2
@Service
public class SeniorsService {
    @Autowired
    private SeniorsMapper seniorsMapper;
    @Transactional(rollbackFor = RuntimeException.class)
    public String insertAndSetRelations(long guardianIdx, SeniorRequestDto seniorReq) {

        String inviteCode = this.generateUniqueID();
        SeniorRequestDto seniorInfo = seniorReq.builder()
                .name(seniorReq.getName())
                .phoneNumber(seniorReq.getPhoneNumber())
                .birth(seniorReq.getBirth())
                .age(seniorReq.getAge())
                .address(seniorReq.getAddress())
                .type(SENIOR)
                .inviteCode(inviteCode)
                .isDisease(seniorReq.getIsDisease())
                .diseases(seniorReq.getDiseases())
                .build();
        // selectKey로 userIdx 채워줌
        int insertCount = seniorsMapper.insertDetail(seniorInfo);
        if (insertCount != 1) {
            log.error("insert Senior ERROR! info from userdetail table is null {}", seniorInfo);
            throw new RuntimeException(
                    "insert Senior ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + seniorInfo);
        }

        int insertNameCount = seniorsMapper.update(seniorInfo.getUserIdx(), seniorInfo.getName(), seniorInfo.getType());
        if (insertNameCount != 1) {
            log.error("update Senior info ERROR! info from user table is null {}", seniorInfo);
            throw new RuntimeException(
                    "update Senior info ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + seniorInfo);
        }
        if(Boolean.TRUE.equals(seniorInfo.getIsDisease()))
        {
            int insertDiseasesCount = seniorsMapper.insertDiseases(seniorInfo.getUserIdx(), seniorInfo.getDiseases());
            // 두 개의 행을 성공적으로 추가
            if(insertDiseasesCount != 2) {
                log.error("insert Disease during register ERROR! guadianIdx : ", guardianIdx);
                throw new RuntimeException(
                        "insert Disease during register ERROR! 회원가입 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx + "seniorIdx : " + seniorInfo.getUserIdx());
            }
        }

        int relationInsertCount = seniorsMapper.insertRelationWithSenior(guardianIdx, seniorInfo.getUserIdx());
        if(relationInsertCount != 1) {
            log.error("set Senior Relation during register ERROR! guadianIdx : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 회원가입 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx + "seniorIdx : " + seniorInfo.getUserIdx());
        }

        return inviteCode;
    }
    public UsersResponseDto loginByInviteCode(String inviteCode) {
        UsersResponseDto userInfo = seniorsMapper.findByInviteCode(inviteCode);
        if(userInfo == null){
            log.error("login Senior ERROR! userinfo is null {}", inviteCode);
            throw new DuplicateIdException("login Senior ERROR! 회원정보가 없습니다.\n" + "Params : " + inviteCode);
        }

        return userInfo;
    }

    public void updateFCM(long userIdx, String fcmToken) {
        int updateCount = seniorsMapper.updateFCM(userIdx, fcmToken);
        if(updateCount == 0){
            log.error("login Senior ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("login Senior ERROR! FCM 저장에 실패했습니다.\n" + "FCM Token : " + fcmToken);
        }
    }
    public FamilyResponseDto getGuardiansAndMyinfo(long userIdx){
        List<GuardianDetailResponseDto> guardianDetailList = seniorsMapper.findGuardiansByIdx(userIdx);
        SeniorDetailResponseDto myInfo = seniorsMapper.findByIdx(userIdx);

        FamilyResponseDto familyInfo = FamilyResponseDto.builder()
                .guardianDetailResponseDtos(guardianDetailList)
                .seniorDetailResponseDtos(myInfo.toList())
                .build();
        return familyInfo;
    }

    public GuardianDetailResponseDto findGuardianByGuardianId(String id) {
        GuardianDetailResponseDto guardians = seniorsMapper.findGuardianByGuardianId(id);

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

    private static String generateUniqueID() {
        // UUID 생성
        UUID uuid = UUID.randomUUID();
        // UUID를 문자열로 변환하여 하이픈(-)을 제거하고 필요한 길이로 잘라줍니다.
        String uniqueID = uuid.toString().replaceAll("-", "").substring(0, 7);

        return uniqueID;
    }

    public void updateProfile(long userIdx, SeniorProfileRequestDto profileRequest){
        int updateCount = seniorsMapper.updateProfile(userIdx, profileRequest.getName(), profileRequest.getProfileImageUrl(), profileRequest.getBirth(), profileRequest.getPhoneNumber(), profileRequest.getAddress(), profileRequest.getIsDisease());
        if(updateCount == 0){
            log.error("update senior profile ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("update senior profile ERROR! \n" + "update userIdx : " + userIdx);
        }
        profileRequest.getDiseases().stream().forEach(disease -> seniorsMapper.updateDisease(userIdx, disease));
    }
}

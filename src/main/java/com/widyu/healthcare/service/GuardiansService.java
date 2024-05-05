package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.request.GuardianProfileRequestDto;
import com.widyu.healthcare.dto.request.GuardianRequestDto;
import com.widyu.healthcare.dto.response.*;
import com.widyu.healthcare.error.exception.DuplicateIdException;
import com.widyu.healthcare.mapper.GuardiansMapper;
import com.widyu.healthcare.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.widyu.healthcare.aop.LoginCheck.UserType.GUARDIAN;

@Service
@Log4j2
public class GuardiansService {
    @Autowired
    private GuardiansMapper guardiansMapper;

    public GuardianDetailResponseDto insert(GuardianRequestDto guardianReq) {
        boolean duplIdResult = isDuplicatedId(guardianReq.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        guardianReq.builder()
                .id(guardianReq.getId())
                .password(SHA256Util.encryptSHA256(guardianReq.getPassword()))
                .phoneNumber(guardianReq.getPhoneNumber())
                .build();
        long insertCount = guardiansMapper.insertDetail(guardianReq);
        if (insertCount != 1) {
            log.error("insert Guardiance ERROR! {}", guardianReq);
            throw new RuntimeException(
                    "insert Guardiance ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + guardianReq);
        }
        int insertNameCount = guardiansMapper.update(guardianReq.getUserIdx(), guardianReq.getName());
        if (insertNameCount != 1) {
            log.error("update Guardiance info ERROR! info from user table is null {}", guardianReq);
            throw new RuntimeException(
                    "update Guardiance info ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + guardianReq);
        }
        GuardianDetailResponseDto userInfo = GuardianDetailResponseDto
                .builder()
                .userIdx(guardianReq.getUserIdx())
                .name(guardianReq.getName())
                .id(guardianReq.getId())
                .phoneNumber(guardianReq.getPassword())
                .build();
        return userInfo;
    }
    public UsersResponseDto loginByIdAndPassword(String id, String password) {
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        UsersResponseDto userInfo = guardiansMapper.findByIdAndPassword(id, cryptoPassword);
        if(userInfo == null){
            throw new DuplicateIdException("login Guardian ERROR! 회원정보가 없습니다.\n");
        }
        return userInfo;
    }

    public void updateFCM(long userIdx, String fcmToken) {
        int updateCount = guardiansMapper.updateFCM(userIdx, fcmToken);
        if(updateCount == 0){
            log.error("login Guardian ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("login Senior ERROR! FCM 저장에 실패했습니다.\n" + "FCM Token : " + fcmToken);
        }
    }

    public GuardianDetailResponseDto findId(String name, String phoneNumber){
        GuardianDetailResponseDto userInfo = guardiansMapper.findIdByNameAndNumber(name, phoneNumber);
        if(userInfo == null){
            throw new DuplicateIdException("아이디 찾기 Guardian ERROR! 회원정보가 없습니다.\n");
        }

        return userInfo;
    }

    public GuardianDetailResponseDto findPassword(String id, String newPassword, String name, String phoneNumber){
        GuardianDetailResponseDto userInfo = guardiansMapper.updatePasswordByGuardianInfos(id, SHA256Util.encryptSHA256(newPassword), name, phoneNumber);
        if(userInfo == null){
            throw new DuplicateIdException("비밀번호 찾기 Guardian ERROR! 회원정보가 없습니다.\n");
        }
        return userInfo;
    }

    public boolean isDuplicatedId(String id) {
        return guardiansMapper.checkId(id) == 1;
    }

    public FamilyResponseDto getSeniorsAndMyInfo(long userIdx){
        GuardianDetailResponseDto myInfo = guardiansMapper.findByIdx(userIdx);
        List<SeniorDetailResponseDto> seniorDetailList = guardiansMapper.findSeniorsByIdx(userIdx);

        FamilyResponseDto familyInfo = FamilyResponseDto.builder()
                .guardianDetailResponseDtos(myInfo.toList())
                .seniorDetailResponseDtos(seniorDetailList)
                .build();
        return familyInfo;
    }

    public List<SeniorDetailResponseDto>  getSeniorsOrNull(long userIdx){
        GuardianDetailResponseDto myInfo = guardiansMapper.findByIdx(userIdx);
        List<SeniorDetailResponseDto> seniorDetailList = guardiansMapper.findSeniorsByIdx(userIdx);

        return seniorDetailList;
    }

    public void updateProfile(long userIdx, GuardianProfileRequestDto profileReq){
        int updateCount = guardiansMapper.updateProfile(userIdx, profileReq.getName(), profileReq.getProfileImageUrl(), profileReq.getPhoneNumber(), profileReq.getAddress(), profileReq.getBirth());
        if(updateCount == 0){
            log.error("update Guardian profile ERROR! update fail, count is {}", updateCount);
            throw new DuplicateIdException("update Guardian profile ERROR! \n" + "update : " + profileReq);
        }
    }
}

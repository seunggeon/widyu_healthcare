package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.domain.DiseaseDto;
import com.widyu.healthcare.dto.UserType;
import com.widyu.healthcare.dto.request.SeniorRequestDto;
import com.widyu.healthcare.dto.response.GuardianDetailResponseDto;
import com.widyu.healthcare.dto.response.SeniorDetailResponseDto;
import com.widyu.healthcare.dto.response.UsersResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeniorsMapper {

    public int insertDetail(SeniorRequestDto encryptedUser);
    public int update(@Param("userIdx") long seniorIdx, @Param("name") String name, @Param("type") UserType type);
    public int insertDiseases(@Param("userIdx") long userIdx, @Param("diseases") List<DiseaseDto> diseases);
    public int insertRelationWithSenior(@Param("guardianIdx") long guardianIdx, @Param("seniorIdx") long seniorIdx);
    public int updateFCM(@Param("seniorIdx") long seniorIdx, @Param("fcmToken") String fcmToken);
    public UsersResponseDto findByInviteCode(@Param("inviteCode") String inviteCode);
    public SeniorDetailResponseDto findByIdx(@Param("userIdx") long userIdx);
    public List<GuardianDetailResponseDto> findGuardiansByIdx(@Param("userIdx") long userIdx);
    public GuardianDetailResponseDto findGuardianByGuardianId(@Param("id") String id);
    public int updateProfile(@Param("userIdx") long userIdx, @Param("name") String name, @Param("profileImageUrl") String profileImageUrl, @Param("birth") String birth, @Param("phoneNumber") String phoneNumber, @Param("address") String address, @Param("isDisease") int isDisease);
    public void updateDisease(@Param("userIdx") long userIdx, @Param("disease") DiseaseDto diseases);

}
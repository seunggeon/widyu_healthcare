package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.request.GuardianRequestDto;
import com.widyu.healthcare.dto.response.GuardianDetailResponseDto;
import com.widyu.healthcare.dto.response.SeniorDetailResponseDto;
import com.widyu.healthcare.dto.response.UsersResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuardiansMapper {

    public int checkId(String id);
    public long insert(GuardianRequestDto encryptedUser);
    public int updateFCM(long guardianIdx, String fcmToken);
    public UsersResponseDto findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    public GuardianDetailResponseDto findByIdx(@Param("userIdx") long userIdx);
    public List<SeniorDetailResponseDto> findSeniorsByIdx(@Param("userIdx") long userIdx);
    public List<Long> findSeniorsIdxByIdx(@Param("guardianIdx") long userIdx);
    public GuardianDetailResponseDto findIdByNameAndNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber);
    public GuardianDetailResponseDto updatePasswordByGuardianInfos(@Param("id") String id, @Param("newPassword") String newPassword, @Param("name") String name, @Param("phoneNumber") String phoneNumber);
    public int updateProfile(@Param("userIdx") long userIdx, @Param("name") String name, @Param("profileImageUrl") String profileImageUrl, @Param("phoneNumber") String phoneNumber, @Param("address") String address, @Param("birth") String birth);
}
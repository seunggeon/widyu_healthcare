package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserType;
import com.widyu.healthcare.core.api.controller.v1.request.senior.RegisterSeniorRequest;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeniorsMapper {

    public int insertDetail(User encryptedUser);
    public int update(@Param("userIdx") long seniorIdx, @Param("name") String name, @Param("type") UserType type);
    public int insertDiseases(@Param("userIdx") long userIdx, @Param("diseases") List<Disease> diseases);
    public int insertRelationWithSenior(@Param("guardianIdx") long guardianIdx, @Param("seniorIdx") long seniorIdx);
    public int updateFCM(@Param("seniorIdx") long seniorIdx, @Param("fcmToken") String fcmToken);
    public String findFCM(@Param("userIdx") long userIdx);
    public CommonUserResponse findByInviteCode(@Param("inviteCode") String inviteCode);
    public SeniorInfoResponse findByIdx(@Param("userIdx") long userIdx);
    public List<GuardianInfoResponse> findGuardiansByIdx(@Param("userIdx") long userIdx);
    public GuardianInfoResponse findGuardianByGuardianId(@Param("id") String id);
    public int updateProfile(@Param("userIdx") long userIdx, @Param("name") String name, @Param("profileImageUrl") String profileImageUrl, @Param("birth") String birth, @Param("phoneNumber") String phoneNumber, @Param("address") String address, @Param("isDisease") Boolean isDisease);
    public void updateDisease(@Param("userIdx") long userIdx, @Param("disease") Disease diseases);

}
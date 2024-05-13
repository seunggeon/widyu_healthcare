package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.api.controller.v1.request.guardian.RegisterGuardianRequest;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.domain.domain.v1.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuardiansMapper {

    public int checkId(String id);
    public long insertDetail(User encryptedUser);
    public int update(@Param("userIdx") long guardianIdx, @Param("name") String name);
    public int updateFCM(long guardianIdx, String fcmToken);
    public CommonUserResponse findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    public GuardianInfoResponse findByIdx(@Param("userIdx") long userIdx);
    public List<SeniorInfoResponse> findSeniorsByIdx(@Param("userIdx") long userIdx);
    public List<Long> findSeniorsIdxByIdx(@Param("guardianIdx") long userIdx);
    public GuardianInfoResponse findIdByNameAndNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber);
    public GuardianInfoResponse updatePasswordByGuardianInfos(@Param("id") String id, @Param("newPassword") String newPassword, @Param("name") String name, @Param("phoneNumber") String phoneNumber);
    public int updateProfile(@Param("userIdx") long userIdx, @Param("name") String name, @Param("profileImageUrl") String profileImageUrl, @Param("phoneNumber") String phoneNumber, @Param("address") String address, @Param("birth") String birth);
}
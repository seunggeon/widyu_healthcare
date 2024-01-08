package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.users.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {

    int checkId(String id);
    public int insertGuardian(UsersDTO encryptedUser);
    public int insertSenior(UsersDTO encryptedUser);
    public UsersDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    public UsersDTO findByInviteCode(@Param("inviteCode") String inviteCode);

}
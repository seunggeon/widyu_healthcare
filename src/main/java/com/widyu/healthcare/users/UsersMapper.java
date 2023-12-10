package com.widyu.healthcare.users;

import com.widyu.healthcare.users.dto.UsersDTO;
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
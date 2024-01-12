package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.users.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersMapper {

    public int checkId(String id);
    public int insertGuardian(UsersDTO encryptedUser);
    public int insertSenior(UsersDTO encryptedUser);
    public UsersDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    public UsersDTO findByInviteCode(@Param("inviteCode") String inviteCode);
    public List<UsersDTO> getAllSeniors(@Param("userIdx") Integer userIdx);
}
package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.users.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeniorsMapper {

    public int insert(UsersDTO encryptedUser);
    public UsersDTO findByInviteCode(@Param("inviteCode") String inviteCode);

}
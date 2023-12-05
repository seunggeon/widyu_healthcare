package com.widyu.healthcare.users;

import com.widyu.healthcare.users.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

    int checkId(String id);

    public int insertUser(UserDTO encryptedUser);
}
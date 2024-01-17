package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.users.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuardiansMapper {

    public int checkId(String id);
    public int insert(UsersDTO encryptedUser);
    public UsersDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);
    public List<UsersDTO> findSeniorsById(@Param("userIdx") Integer userIdx);
}
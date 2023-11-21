package com.widyu.healthcare.users.service;

import com.widyu.healthcare.users.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public int getAllUsers() {
        return userMapper.checkId();
    }

}

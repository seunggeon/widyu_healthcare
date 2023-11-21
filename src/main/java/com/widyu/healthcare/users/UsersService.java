package com.widyu.healthcare.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersMapper usersMapper;

    @Autowired
    public UsersService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    public int getAllUsers() {
        return usersMapper.checkId();
    }

}

package com.widyu.healthcare.service;

import com.widyu.healthcare.error.exception.DuplicateIdException;
import com.widyu.healthcare.mapper.UsersMapper;
import com.widyu.healthcare.utils.SHA256Util;
import com.widyu.healthcare.dto.users.UsersDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.widyu.healthcare.aop.LoginCheck.UserType.GUARDIAN;

@Service
@Log4j2
public class GuardianService {
    @Autowired
    private UsersMapper usersMapper;

    public void insert(UsersDTO userInfo) {
        boolean duplIdResult = isDuplicatedId(userInfo.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        userInfo.setType(GUARDIAN);
        userInfo.setPassword(SHA256Util.encryptSHA256(userInfo.getPassword()));
        int insertCount = usersMapper.insertGuardian(userInfo);

        if (insertCount != 1) {
            log.error("insert Guardiance ERROR! {}", userInfo);
            throw new RuntimeException(
                    "insert Guardiance ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userInfo);
        }
    }
    public UsersDTO login(String id, String password) {
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        UsersDTO userInfo = usersMapper.findByIdAndPassword(id, cryptoPassword);
        return userInfo;
    }
    public boolean isDuplicatedId(String id) {
        return usersMapper.checkId(id) == 1;
    }
}

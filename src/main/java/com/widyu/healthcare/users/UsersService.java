package com.widyu.healthcare.users;

import com.widyu.healthcare.global.error.exception.DuplicateIdException;
import com.widyu.healthcare.global.utils.SHA256Util;
import com.widyu.healthcare.users.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    @Service 어노테이션은 비즈니스 로직을 처리하는 서비스라는 것을 알려주는 어노테이션이다.
    Component Scan을 통하여 @Service 어노테이션이 붙은 클래스를 스프링이 빈으로 등록하고 이 빈의 라이프사이클을 관리한다.
*/

@Service
@Log4j2
public class UsersService {
    @Autowired
    private  UsersMapper usersMapper;

    public void insertUser(UserDTO userInfo) {
        // id 중복체크
        boolean duplIdResult = isDuplicatedId(userInfo.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }

        userInfo.setPassword(SHA256Util.encryptSHA256(userInfo.getPassword()));
        int insertCount = usersMapper.insertUser(userInfo);

        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userInfo);
            throw new RuntimeException(
                    "insertMember ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userInfo);
        }
    }
    public boolean isDuplicatedId(String id) {
        return usersMapper.checkId(id) == 1;
    }


}

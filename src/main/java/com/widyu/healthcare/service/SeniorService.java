package com.widyu.healthcare.service;

import java.util.UUID;

import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.mapper.UsersMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.widyu.healthcare.aop.LoginCheck.UserType.SENIOR;

@Service
@Log4j2
public class SeniorService {
    @Autowired
    private UsersMapper usersMapper;

    public String insertAndGetInviteCode(UsersDTO userInfo) {

        String inviteCode = this.generateUniqueID();
        userInfo.setInviteCode(inviteCode);
        userInfo.setType(SENIOR);

        int insertCount = usersMapper.insertSenior(userInfo);

        if (insertCount != 1) {
            inviteCode = null;
            log.error("insert Senior ERROR! {}", userInfo);
            throw new RuntimeException(
                    "insert Senior ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userInfo);
        }
        return inviteCode;
    }
    public UsersDTO login(String inviteCode) {
        UsersDTO userInfo = usersMapper.findByInviteCode(inviteCode);
        return userInfo;
    }

    private static String generateUniqueID() {
        // UUID 생성
        UUID uuid = UUID.randomUUID();

        // UUID를 문자열로 변환하여 하이픈(-)을 제거하고 필요한 길이로 잘라줍니다.
        String uniqueID = uuid.toString().replaceAll("-", "").substring(0, 7);

        return uniqueID;
    }
}

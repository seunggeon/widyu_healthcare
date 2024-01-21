package com.widyu.healthcare.service;

import java.util.UUID;

import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.mapper.SeniorsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.widyu.healthcare.error.exception.DuplicateIdException;
import org.springframework.transaction.annotation.Transactional;

import static com.widyu.healthcare.aop.LoginCheck.UserType.SENIOR;

@Log4j2
@Service
public class SeniorsService {
    @Autowired
    private SeniorsMapper seniorsMapper;
    @Transactional(rollbackFor = RuntimeException.class)
    public UsersDTO insertAndSetRelations(long guardianIdx, UsersDTO seniorInfo) {

        String inviteCode = this.generateUniqueID();
        seniorInfo.setInviteCode(inviteCode);
        seniorInfo.setType(SENIOR);

        // selectKey로 userIdx 채워줌
        int insertCount = seniorsMapper.insert(seniorInfo);
        if (insertCount != 1) {
            inviteCode = null;
            log.error("insert Senior ERROR! userinfo is null {}", seniorInfo);
            throw new RuntimeException(
                    "insert Senior ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + seniorInfo);
        }
        int relationInsertCount = seniorsMapper.insertRelationWithSenior(guardianIdx, seniorInfo.userIdx);
        if(relationInsertCount != 1) {
            log.error("set Senior Relation during register ERROR! guadianIdx : ", guardianIdx);
            throw new RuntimeException(
                    "set Senior Relation during register ERROR! 회원가입 메서드를 확인해주세요\n" + "guadianIdx : " + guardianIdx);
        }

        return seniorInfo;
    }
    public UsersDTO loginByInviteCode(String inviteCode) {
        UsersDTO userInfo = seniorsMapper.findByInviteCode(inviteCode);
        if(userInfo == null){
            log.error("login Senior ERROR! userinfo is null {}", inviteCode);
            throw new DuplicateIdException("login Senior ERROR! 회원정보가 없습니다.\n" + "Params : " + inviteCode);
        }

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

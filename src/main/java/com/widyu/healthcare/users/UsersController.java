package com.widyu.healthcare.users;

import com.google.firebase.database.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.widyu.healthcare.users.dto.UserDTO;
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
//    @Autowired
    private final UsersService usersService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @NotNull UserDTO userInfo) {
        if (UserDTO.hasNullDataBeforeSignup(userInfo)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        usersService.insertUser(userInfo);
    }
    //
//    @GetMapping("myInfo")
//    @LoginCheck(type = UserType.MEMBER)
//
//    // 문자 인증 번호 보내기
//
//    // 문자 인증 번호 확인 및 부양자 중복 회원 체크 유무
//
//    // 부양자 회원가입 : 아이디, 비번만
//
//    // 부양자 로그인 : 아이디, 비번
//
//    // 시니어 회원가입 : 이름, 출생년도, 연락처, 집주소 -> 생성된 시니어 아이디를 부양자 테이블에 id update
//
//    // 초대 코드 발급
//
//    // 시니어 로그인 : 초대 코드 확인
//
//    // 부양자 아이디로 조회
//
//    // 아이디 찾기 : 이름 연락처 -> 아이디
//
//    // 비밀번호 찾기 : 아이디 -> 비번
//
//    // 시니어 추가 -> 회원가입 api로 대체
//
//    // 내 시니어 모두 조회
//
//    // 부양자 연락처 수정
}

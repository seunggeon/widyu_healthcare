package com.widyu.healthcare.aop;

import jakarta.servlet.http.HttpSession;
import com.widyu.healthcare.utils.SessionUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Log4j2
@SuppressWarnings("unchecked")
public class AuthCheckAspect {
    @Before("@annotation(com.widyu.healthcare.aop.GuardianLoginCheck)") // 파일경로에 있는 어노테이션 인터페이스가 호출되면 어노테이션을 실행하기 전에 메소드를 실행한다.(=구현부와 같은 역할)
    public void GuardianLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - User Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null) {
            throw new IllegalStateException("Session is null");
        }
        long id = SessionUtil.getLoginGuardianId(session);
        if (id == 0) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "id is null from server session") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.aop.SeniorLoginCheck)")
    public void SeniorLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - User Login Check Started");
        // Request session으로부터 Id 값 가져오기
        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null) {
            throw new IllegalStateException("Session is null");
        }
        long id = SessionUtil.getLoginSeniorId(session);
        if (id == 0) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "id is null from server session") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.aop.LoginCheck) && @annotation(loginCheck)")
    public void loginCheck(JoinPoint jp, LoginCheck loginCheck) throws Throwable {
            log.debug("AOP - Login Check Started");

            if (LoginCheck.UserType.GUARDIAN.equals(loginCheck.type())) {
                GuardianLoginCheck(jp);
            }
            else if (LoginCheck.UserType.SENIOR.equals(loginCheck.type())) {
                SeniorLoginCheck(jp);
            }
        }
}
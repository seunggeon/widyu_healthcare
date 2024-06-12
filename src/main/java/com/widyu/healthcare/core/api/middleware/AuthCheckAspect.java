package com.widyu.healthcare.core.api.middleware;

import com.widyu.healthcare.support.error.exception.LoginSessionNullException;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Log4j2
public class AuthCheckAspect {
    @Before("@annotation(com.widyu.healthcare.core.api.middleware.GuardianLoginCheck)") // 파일경로에 있는 어노테이션 인터페이스가 호출되면 어노테이션을 실행하기 전에 메소드를 실행한다.
    public void GuardianLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - Guardian Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null) {
            throw new LoginSessionNullException("Session is null");
        }
        long id = SessionUtil.getLoginGuardianIdx(session);
        if (id == 0) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "id is null from server session") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.core.api.middleware.SeniorLoginCheck)")
    public void SeniorLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - Senior Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null) {
            throw new LoginSessionNullException("Session is null");
        }
        long id = SessionUtil.getLoginSeniorIdx(session);
        if (id == 0) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "id is null from server session") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.core.api.middleware.CommonLoginCheck)")
    public void CommonLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - Common Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null) {
            throw new LoginSessionNullException("Session is null");
        }

        long commonIdx = SessionUtil.getLoginCommonIdx(session);

        if (commonIdx == 0) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "id is null from server session") {};
        }
    }

    @Before("@annotation(com.widyu.healthcare.core.api.middleware.LoginCheck) && @annotation(loginCheck)")
    public void loginCheck(JoinPoint jp, LoginCheck loginCheck) throws Throwable {
            log.debug("AOP - Login Check Started");

            if (LoginCheck.UserType.GUARDIAN.equals(loginCheck.type())) {
                GuardianLoginCheck(jp);
            }
            else if (LoginCheck.UserType.SENIOR.equals(loginCheck.type())) {
                SeniorLoginCheck(jp);
            }
            else if (LoginCheck.UserType.COMMON.equals(loginCheck.type())) {
                CommonLoginCheck(jp);
            }
        }
}
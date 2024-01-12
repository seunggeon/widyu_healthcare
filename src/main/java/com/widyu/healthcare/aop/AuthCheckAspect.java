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
    @Before("@annotation(com.widyu.healthcare.aop.GuardianLoginCheck)")
    public void GuardianLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - User Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        Integer id = SessionUtil.getLoginGuardianId(session);

        if (id == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.aop.SeniorLoginCheck)")
    public void SeniorLoginCheck(JoinPoint jp) throws Throwable {
        log.debug("AOP - User Login Check Started");

        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        Integer id = SessionUtil.getLoginSeniorId(session);

        if (id == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {};
        }
    }
    @Before("@annotation(com.widyu.healthcare.aop.LoginCheck) && @annotation(loginCheck)")
    public void loginCheck(JoinPoint jp, LoginCheck loginCheck) throws Throwable {
            log.debug("AOP - Login Check Started");

            HttpSession session =
            ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest()
            .getSession();

            if (LoginCheck.UserType.GUARDIAN.equals(loginCheck.type())) {
                GuardianLoginCheck(jp);
            }
            else if (LoginCheck.UserType.SENIOR.equals(loginCheck.type())) {
                SeniorLoginCheck(jp);
            }
        }
}
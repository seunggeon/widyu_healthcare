package com.widyu.healthcare.support.utils;

import com.widyu.healthcare.support.error.exception.MisMatchLoginException;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String LOGIN_GUADIANCE_ID = "LOGIN_GUADIANCE_ID";
    private static final String LOGIN_SENIOR_ID = "LOGIN_SENIOR_ID";

    public static long getLoginCommonIdx(HttpSession session){
        try {
            if (session.getAttribute(LOGIN_GUADIANCE_ID) != null)
                return (long) session.getAttribute(LOGIN_GUADIANCE_ID);
            else
                return (long) session.getAttribute(LOGIN_SENIOR_ID);
        } catch (RuntimeException e) {
            throw new MisMatchLoginException("session 정보와 로그인 정보가 일치하지 않습니다.");
        }

    }
    public static long getLoginGuardianIdx(HttpSession session) {
        try {
            return (long) session.getAttribute(LOGIN_GUADIANCE_ID);
        } catch (RuntimeException e) {
            throw new MisMatchLoginException("session 정보와 로그인 정보가 일치하지 않습니다.");
        }
    }
    public static long getLoginSeniorIdx(HttpSession session) {
        try {
            return (long) session.getAttribute(LOGIN_SENIOR_ID);
        } catch (RuntimeException e) {
            throw new MisMatchLoginException("session 정보와 로그인 정보가 일치하지 않습니다.");
        }
    }
    public static void setLoginGuardianIdx(HttpSession session, long userIdx) {
        session.setAttribute(LOGIN_GUADIANCE_ID, userIdx);
        session.setMaxInactiveInterval(3600);
    }
    public static void setLoginSeniorIdx(HttpSession session, long userIdx) {
        session.setAttribute(LOGIN_SENIOR_ID, userIdx);
        session.setMaxInactiveInterval(3600);
    }
    public static void logoutGuardian(HttpSession session) {
        session.removeAttribute(LOGIN_GUADIANCE_ID);
    }
    public static void logoutSenior(HttpSession session) {
        session.removeAttribute(LOGIN_SENIOR_ID);
    }
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
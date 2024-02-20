package com.widyu.healthcare.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String LOGIN_GUADIANCE_ID = "LOGIN_GUADIANCE_ID";
    private static final String LOGIN_SENIOR_ID = "LOGIN_SENIOR_ID";

    // 인스턴스화 방지
    private SessionUtil() {}
    public static long getLoginGuardianId(HttpSession session) {
        return (long) session.getAttribute(LOGIN_GUADIANCE_ID);
    }
    public static void setLoginGuardianId(HttpSession session, long userIdx) {
        session.setAttribute(LOGIN_GUADIANCE_ID, userIdx);
    }
    public static void logoutGuardian(HttpSession session) {
        session.removeAttribute(LOGIN_GUADIANCE_ID);
    }
    public static long getLoginSeniorId(HttpSession session) {
        System.out.println("userIdx" + session.getAttribute(LOGIN_SENIOR_ID));
        return (long) session.getAttribute(LOGIN_SENIOR_ID);
    }
    public static void setLoginSeniorId(HttpSession session, long userIdx) {
        session.setAttribute(LOGIN_SENIOR_ID, userIdx);
    }
    public static void logoutSenior(HttpSession session) {
        session.removeAttribute(LOGIN_SENIOR_ID);
    }
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
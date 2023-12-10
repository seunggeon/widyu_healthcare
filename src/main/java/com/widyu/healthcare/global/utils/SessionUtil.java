package com.widyu.healthcare.global.utils;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;

public class SessionUtil {

    private static final String LOGIN_GUADIANCE_ID = "LOGIN_GUADIANCE_ID";
    private static final String LOGIN_SENIOR_ID = "LOGIN_SENIOR_ID";

    // 인스턴스화 방지
    private SessionUtil() {}
    public static String getLoginGuardianId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_GUADIANCE_ID);
    }
    public static void setLoginGuardianId(HttpSession session, String id) {
        session.setAttribute(LOGIN_GUADIANCE_ID, id);
    }
    public static void logoutGuardian(HttpSession session) {
        session.removeAttribute(LOGIN_GUADIANCE_ID);
    }
    public static String getLoginSeniorId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_SENIOR_ID);
    }
    public static void setLoginSeniorId(HttpSession session, String id) {
        session.setAttribute(LOGIN_SENIOR_ID, id);
    }
    public static void logoutSenior(HttpSession session) {
        session.removeAttribute(LOGIN_SENIOR_ID);
    }
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
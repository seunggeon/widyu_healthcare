package com.widyu.healthcare.utils;

import jakarta.servlet.http.HttpSession;
import com.widyu.healthcare.dto.users.UsersDTO;
import lombok.NonNull;

public class SessionUtil {

    private static final String LOGIN_GUADIANCE_ID = "LOGIN_GUADIANCE_ID";
    private static final String LOGIN_SENIOR_ID = "LOGIN_SENIOR_ID";

    // 인스턴스화 방지
    private SessionUtil() {}
    public static Integer getLoginGuardianId(HttpSession session) {
        return (Integer) session.getAttribute(LOGIN_GUADIANCE_ID);
    }
    public static void setLoginGuardianId(HttpSession session, Integer userIdx) {
        session.setAttribute(LOGIN_GUADIANCE_ID, userIdx);
    }
    public static void logoutGuardian(HttpSession session) {
        session.removeAttribute(LOGIN_GUADIANCE_ID);
    }
    public static Integer getLoginSeniorId(HttpSession session) {
        return (Integer) session.getAttribute(LOGIN_SENIOR_ID);
    }
    public static void setLoginSeniorId(HttpSession session, Integer userIdx) {
        session.setAttribute(LOGIN_SENIOR_ID, userIdx);
    }
    public static void logoutSenior(HttpSession session) {
        session.removeAttribute(LOGIN_SENIOR_ID);
    }
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
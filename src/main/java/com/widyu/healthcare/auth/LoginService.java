package com.widyu.healthcare.auth;
public interface LoginService {

    void loginUser(String id);

    void logoutUser();

    String getCurrentUser();

}
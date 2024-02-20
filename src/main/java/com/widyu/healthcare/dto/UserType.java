package com.widyu.healthcare.dto;

public enum UserType {
    GUARDIAN("보호자"),
    SENIOR("시니어");
    private String userType;
    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}

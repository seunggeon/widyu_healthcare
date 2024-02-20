package com.widyu.healthcare.dto;

public enum UserStatus {
    ACTIVE("활성화"),
    DELETE("삭제");
    private String userStatus;
    UserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }
}

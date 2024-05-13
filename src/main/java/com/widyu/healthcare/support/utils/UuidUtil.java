package com.widyu.healthcare.support.utils;

import java.util.UUID;

public class UuidUtil {
    public static String generateUniqueID() {
        // UUID 생성
        UUID uuid = UUID.randomUUID();
        // UUID를 문자열로 변환하여 하이픈(-)을 제거하고 필요한 길이로 잘라줍니다.
        String uniqueID = uuid.toString().replaceAll("-", "").substring(0, 7);

        return uniqueID;
    }
}

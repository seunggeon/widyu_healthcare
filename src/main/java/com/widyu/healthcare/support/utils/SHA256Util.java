package com.widyu.healthcare.support.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.Level;
import lombok.extern.log4j.Log4j2;

// 암호화 방식 - SHA-256
/*
 * StringBuffer와 StringBuilder - 동기화가 필요한 경우(멀티 쓰레드 환경) StringBuffer을 사용 - StringBuilder은 동기화를 보장하지
 * 않지만 성능적으로 우수함
 *
 * 동기화를 지원하는 StringBuffer의 경우 thread-safe를 보장하기 위해 block, unblock 처리를 진행한다.
 * 그렇기 때문에 동일한 연산에 동기화 처리를 한 연산과 동기화 처리를 하지 않은 연산은 약 9배 이상의 성능 차이가 발생할 수 있다. - 자바 성능튜닝 이야기
 */
@Log4j2
public class SHA256Util {
    public static final String ENCRYPTION_TYPE = "SHA-256";

    public static String encryptSHA256(String str) {
        String SHA = null;

        MessageDigest sh;
        try {
            sh = MessageDigest.getInstance(ENCRYPTION_TYPE);
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화 에러! SHA256Util 확인 필요 ", e);
        }
        return SHA;
    }
}
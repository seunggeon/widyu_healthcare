package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.db.client.mapper.RedisMapper;
import com.widyu.healthcare.support.error.exception.FailSmsSendException;
import com.widyu.healthcare.support.error.exception.MisMatchSmsCodeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Log4j2
@Service
@RequiredArgsConstructor
public class SmsService {

    private final RedisMapper redisMapper;
    private final DefaultMessageService messageService;
    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";

    public SingleMessageSentResponse sendCodeAndSaveRedis(String phoneNumber, String code) {
        SingleMessageSentResponse response;
        try{
            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
            message.setFrom("01050944289");
            message.setTo(phoneNumber);
            message.setText("Widyu 문자 인증 번호는" + "["+code+"]" + " 입니다. 인증 번호를 입력해주세요");

            response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e){
            log.error("coolsms 서버 문자 전송 실패\n",e);
            throw new FailSmsSendException("coolsms 서버 문자 전송 실패\n");
        }

        this.saveCertificationCode(phoneNumber, code);
        return response;
    }
    public String verifyCode(String phoneNumber, String code){
        List<String> cerNumList = this.getCertificationCode(phoneNumber);
        String json_code = "\"" + code + "\"";
        if(cerNumList.contains(json_code)){
            this.deleteCertificationCode(phoneNumber);
            return "문자 인증 성공";
        } else throw new MisMatchSmsCodeException("일치하지 않는 인증 번호\n");
    }
    public String generateCerNum(){
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        return numStr;
    }
    public void saveCertificationCode(String phoneNumber, String code) {
        String key = buildRedisKey(phoneNumber);
        this.redisMapper.setListValue(key, code);
    }
    public List<String> getCertificationCode(String phoneNumber) {
        String key = buildRedisKey(phoneNumber);
        return this.redisMapper.getListValueByKey(key);
    }
    public void deleteCertificationCode(String phoneNumber) {
        String key = buildRedisKey(phoneNumber);
//        this.redisService.getListValueByKey(key);
    }
    private static String buildRedisKey(String phoneNumber) {
        return VERIFICATION_CODE_PREFIX + phoneNumber;
    }
}

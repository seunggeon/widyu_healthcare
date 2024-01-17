package com.widyu.healthcare.service;

import com.widyu.healthcare.error.exception.DuplicateIdException;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Log4j2
@Service
public class SmsService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DefaultMessageService messageService;
    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";

    public SingleMessageSentResponse sendCodeAndSaveRedis(String phoneNumber, String code) {
        SingleMessageSentResponse response;
        try{
            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
            message.setFrom("07041416666");
            message.setTo(phoneNumber);
            message.setText("[widyu] 본인 인증: 인증번호는" + "["+code+"]" + "입니다.");

            response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e){
            log.error("누리고 서버 문자 전송 실패\n",e);
            throw new DuplicateIdException("누리고 서버 문자 전송 실패\n");
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
        } else throw new DuplicateIdException("일치하지 않는 인증 번호\n");
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
        this.redisService.setValue(key, code, 5L);
    }
    public List<String> getCertificationCode(String phoneNumber) {
        String key = buildRedisKey(phoneNumber);
        return this.redisService.getListValueByKey(key);
    }
    public void deleteCertificationCode(String phoneNumber) {
        String key = buildRedisKey(phoneNumber);
//        this.redisService.getListValueByKey(key);
    }
    private static String buildRedisKey(String phoneNumber) {
        return VERIFICATION_CODE_PREFIX + phoneNumber;
    }
}

package com.widyu.healthcare.core.db.client.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RedisMapper {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ListOperations<String, Object> listOps;
    private final ValueOperations<String, Object> stringOps;
    public RedisMapper(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
        this.stringOps = redisTemplate.opsForValue();
    }
    @SneakyThrows
    public List<String> getListValueByKey(String key) {
        List<String> stringList = new ArrayList<>();
        List<Object> ResultRange = listOps.range(key, 0, 9);
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object obj : ResultRange) {
            try {
                // ObjectMapper를 사용하여 각 객체를 JSON 문자열로 변환한 후 String으로 저장
                String jsonString = objectMapper.writeValueAsString(obj);
                stringList.add(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
                // 변환에 실패한 경우에 대한 예외 처리
            }
        }
        return stringList;
    }
    @SneakyThrows
    public <T> T getValueByKeyOrNull(String key, Class<T> classType) {
        try {
            String jsonResult = (String) stringOps.get(key);
            if (jsonResult == null) {
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            T obj = objectMapper.readValue(jsonResult, classType);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @SneakyThrows
    public void setListValue(String key, String value){
        listOps.leftPush(key, value);
    }
    @SneakyThrows
    public void setValue(String key, String value, Long expirationTime){
        if(expirationTime != null){
            stringOps.set(key, value, expirationTime);
        }else {
            stringOps.set(key, value);
        }
    }

    public Long getPoint(String key) {
        Object point = stringOps.get(key);
        return point != null ? Long.parseLong(point.toString()) : 0L;
    }

    public void incrementPoint(String key, Long point) {
        Long currentPoint = getPoint(key);
        stringOps.increment(key, currentPoint + point);
    }

    public void setPoint(String key, Long point) {
        stringOps.set(key, Long.toString(point));
    }

    public void decrementPoint(String key, Long point) {
//        stringOps.decrement(key, point);
    }

}


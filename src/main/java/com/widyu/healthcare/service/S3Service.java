package com.widyu.healthcare.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.widyu.healthcare.dto.goals.Goal;
import com.widyu.healthcare.dto.goals.GoalStatus;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final AmazonS3 amazonS3Client;
    private final GoalsStatusMapper goalsStatusMapper;


    @Autowired
    public S3Service(AmazonS3 amazonS3Client, GoalsStatusMapper goalsStatusMapper, String bucket, String defaultUrl) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
        this.defaultUrl = defaultUrl;
        this.goalsStatusMapper = goalsStatusMapper;
    }


    // 목표 인증 파일 업로드
    public void insertFile(long goalIdx, LocalDateTime time, MultipartFile multipartFile) throws IOException {

        goalsStatusMapper.getGoalStatusByGoalId(goalIdx, time);
        String url = upload(multipartFile);
        goalsStatusMapper.updateGoalStatusUrl(url, goalIdx, time);
    }

    // 목표 인증 파일 삭제
    public void deleteFile(long goalIdx, LocalDateTime time){

        GoalStatus goalStatus = goalsStatusMapper.getGoalStatusByGoalId(goalIdx, time);
        delete(goalStatus.getImgUrl());
        goalsStatusMapper.updateGoalStatusUrl(null, goalIdx, time);
    }

    private String upload(MultipartFile uploadFile) throws IOException {
        String orignalName = uploadFile.getOriginalFilename();
        String url;
        try{
            //확장자를 찾기 위한 코드
            final String ext = orignalName.substring(orignalName.lastIndexOf('.'));
            //파일이름 암호화
            final String saveName = getUuid() + ext;
            //파일 객체 생성
            File file = new File(System.getProperty("user.dir") + saveName);
            //파일 변환
            uploadFile.transferTo(file);
            //S3 파일 업로드
            uploadOnS3(saveName, file);
            //주소 할당
            url = defaultUrl + saveName;
            //파일 삭제
            file.delete();
        } catch (StringIndexOutOfBoundsException e){
            url = null;
        }
        return url;
    }

    //파일 삭제
    public String delete(String url) {

        String result = "success";

        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, url);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, url);
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }
        return result;
    }

    private static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String findName, final File file){
        //AWS S3 전송 객체 생성
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        //요청 객체 생성
        final PutObjectRequest request = new PutObjectRequest(bucket, findName, file);
        //업로드 시도
        final Upload upload = transferManager.upload(request);

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException){
            log.error(amazonClientException.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}

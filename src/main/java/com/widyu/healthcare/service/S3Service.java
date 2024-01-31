package com.widyu.healthcare.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Log4j2
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final AmazonS3 amazonS3Client;
    private final GoalsStatusMapper goalsStatusMapper;


    @Autowired
    public S3Service(AmazonS3 amazonS3Client, GoalsStatusMapper goalsStatusMapper) {
        this.amazonS3Client = amazonS3Client;
        this.goalsStatusMapper = goalsStatusMapper;
    }


    // 목표 인증 파일 업로드
    public void insertFile(long goalStatusIdx, MultipartFile multipartFile) throws IOException {

        String url = upload(multipartFile);
        goalsStatusMapper.updateGoalStatusUrl(url, goalStatusIdx);
    }

    // 목표 인증 파일 삭제
    public void deleteFile(long goalStatusIdx) throws IOException {

        String url = goalsStatusMapper.getUrlByGoalStatusId(goalStatusIdx);
        delete(url.split("/")[3]);
        goalsStatusMapper.updateGoalStatusUrl(null, goalStatusIdx);
    }

    private String upload(MultipartFile multipartFile) throws IOException {

        String fileName = multipartFile.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IOException("S3 file upload ERROR!", e);
        }
        String url =  amazonS3Client.getUrl(bucketName, fileName).toString();

        return url;
    }

    //파일 삭제
    public void delete(String url) throws IOException {

        if(amazonS3Client.doesObjectExist(bucketName, url)){
            throw new FileNotFoundException("S3 file not found ERROR!");
        }

        try {
            amazonS3Client.deleteObject(bucketName, url);
        } catch (SdkClientException e){
            throw new IOException("S3 file delete ERROR!", e);
        }
    }

}

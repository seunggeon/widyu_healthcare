package com.widyu.healthcare.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.widyu.healthcare.dto.reward.RewardDTO;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.mapper.RewardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Log4j2
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final AmazonS3 amazonS3Client;
    private final GoalsStatusMapper goalsStatusMapper;
    private final RewardMapper rewardMapper;


    @Autowired
    public S3Service(AmazonS3 amazonS3Client, GoalsStatusMapper goalsStatusMapper, RewardMapper rewardMapper) {
        this.amazonS3Client = amazonS3Client;
        this.goalsStatusMapper = goalsStatusMapper;
        this.rewardMapper = rewardMapper;
    }


    // 목표 인증 파일 업로드
    public void insertGoalFile(long goalStatusIdx, MultipartFile multipartFile) throws IOException {

        String url = upload(multipartFile);
        goalsStatusMapper.updateGoalStatusUrl(url, goalStatusIdx);
    }

    // 목표 인증 파일 삭제
    public void deleteGoalFile(long goalStatusIdx) throws IOException {

        String url = goalsStatusMapper.getUrlByGoalStatusId(goalStatusIdx);
        delete(url);
        goalsStatusMapper.updateGoalStatusUrl(null, goalStatusIdx);
    }

    // 리워드 파일 업로드
    public void insertRewardFile(RewardDTO rewardDTO, MultipartFile multipartFile) throws IOException {

        String url = upload(multipartFile);
        rewardMapper.insertReward(rewardDTO);
    }

    // 리워드 파일 수정
    public void updateReward(RewardDTO rewardDTO){
        rewardMapper.updateReward(rewardDTO);
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        String url = rewardMapper.getUrlbyRewardId(rewardIdx);
        delete(url);
        rewardMapper.updateRewardUrl(null, rewardIdx);

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
    private void delete(String longUrl) throws IOException {

        String url = longUrl.split("/")[3];

        try {
            amazonS3Client.deleteObject(bucketName, url);
        } catch (SdkClientException e){
            throw new IOException("S3 file delete ERROR!", e);
        }
    }

}

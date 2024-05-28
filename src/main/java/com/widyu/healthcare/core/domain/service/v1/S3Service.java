package com.widyu.healthcare.core.domain.service.v1;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.widyu.healthcare.core.db.mapper.v1.GuardiansMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsStatusMapper;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsMapper;
import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import com.widyu.healthcare.support.error.exception.MissingFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final AmazonS3 amazonS3Client;
    private final GoalsStatusMapper goalsStatusMapper;
    private final GuardiansMapper guardiansMapper;
    private final RewardsMapper rewardsMapper;
    private final RewardsStatusMapper rewardsStatusMapper;
    // 목표 인증 파일 업로드
    public void insertGoalFile(long goalStatusIdx, MultipartFile multipartFile) throws IOException {

        String url = upload(multipartFile);
        goalsStatusMapper.updateGoalStatusUrl(url, goalStatusIdx);
    }

    // 목표 인증 파일 삭제
    public void deleteGoalFile(long goalStatusIdx) throws IOException {

        String url = goalsStatusMapper.getUrlByGoalStatusIdx(goalStatusIdx);
        delete(url);
        goalsStatusMapper.updateGoalStatusUrl(null, goalStatusIdx);
    }

    // 리워드 파일 업로드
    public List<Reward> insertRewardFile(long uploaderIdx, String description, RewardType type, MultipartFile multipartFile) throws IOException {

        String url = upload(multipartFile);
        List<Long> seniorsIdxOnFamily = guardiansMapper.findSeniorsIdxByIdx(uploaderIdx);
        List<Reward> rewardList = new ArrayList<>();

        Reward reward = new Reward(-1, uploaderIdx, description, url, type);
        rewardsMapper.insertReward(reward);
        seniorsIdxOnFamily.forEach(seniorIdx -> {
            reward.setUserIdx(seniorIdx);
            rewardsStatusMapper.insertRewardStatus(reward);
            //rewardsMapper.insertReward(reward);
        });

        return rewardList;
    }

    // 리워드 파일 수정
    public void updateReward(long rewardIdx, String description, RewardType type, MultipartFile multipartFile) throws IOException {

        String oldUrl = rewardsMapper.getUrlByRewardIdx(rewardIdx);
        delete(oldUrl);
        String newUrl = upload(multipartFile);
        rewardsMapper.updateReward(rewardIdx, description, type, newUrl);
    }

    // 리워드 파일 삭제
    public void deleteRewardUrl(long rewardIdx) throws IOException {
        String url = rewardsMapper.getUrlByRewardIdx(rewardIdx);
        delete(url);
        rewardsMapper.updateRewardUrl(null, rewardIdx);
    }

    public String upload(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty())
            throw new MissingFileException("file이 없습니다.");
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

    private void delete(String longUrl) throws IOException {

        String url = longUrl.split("/")[3];

        try {
            amazonS3Client.deleteObject(bucketName, url);
        } catch (SdkClientException e){
            throw new IOException("S3 file delete ERROR!", e);
        }
    }


}
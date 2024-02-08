package com.widyu.healthcare.controller;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.dto.reward.RewardDTO;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.service.S3Service;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    /**
     * 사진 업로드 (약 인증)
     */
    @PostMapping("/goal/insert/{goalStatusIdx}")
    public ResponseEntity<?> insertFile(@PathVariable long goalStatusIdx,
                                     @RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile
    ) throws IOException {

        s3Service.insertGoalFile(goalStatusIdx, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "goalStatus file upload 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사진 삭제 (약 인증)
     */
    @GetMapping("/goal/delete/{goalStatusIdx}")
    public ResponseEntity<?> deleteFile(@PathVariable long goalStatusIdx) throws IOException {
        s3Service.deleteGoalFile(goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "goalStatus file delete 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 업로드
     */
    @PostMapping("/reward/insert")
    public ResponseEntity<?> insertReward(@RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile,
                                          @RequestParam(value = "dto", required = false) @NotNull final RewardDTO rewardDTO
    ) throws IOException {

        s3Service.insertRewardFile(rewardDTO, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "goalStatus file upload 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 수정
     */
    @PatchMapping("/reward/update")
    public ResponseEntity<?> updateReward(@RequestBody RewardDTO rewardDTO){
        s3Service.updateReward(rewardDTO);
        SuccessResponse response = new SuccessResponse(true, "reward 수정 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 삭제
     */
    @DeleteMapping("/reward/delete")
    public ResponseEntity<?> deleteReward(@RequestBody long rewardIdx){
        //s3Service.de
        SuccessResponse response = new SuccessResponse(true, "reward 삭제 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

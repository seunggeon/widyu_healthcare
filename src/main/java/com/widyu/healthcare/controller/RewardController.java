package com.widyu.healthcare.controller;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.dto.reward.RewardDTO;
import com.widyu.healthcare.error.exception.InsufficientPointsException;
import com.widyu.healthcare.error.exception.NoDataException;
import com.widyu.healthcare.service.RewardService;
import com.widyu.healthcare.service.S3Service;
import com.widyu.healthcare.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reward")
public class RewardController {

    @Autowired
    private S3Service s3Service;
    @Autowired
    private RewardService rewardService;


    /**
     * 리워드 전체 목록 조회
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllReward(HttpSession session){

        long userIdx = SessionUtil.getLoginGuardianId(session);
        List<RewardDTO> rewardAllInfo = rewardService.getAllReward(userIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 조회 완료", rewardAllInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 구매
     */
    @GetMapping("/buy/{rewardIdx}")
    public ResponseEntity<?> getReward(@PathVariable Long rewardIdx, HttpSession session) throws InsufficientPointsException {
        long userIdx = SessionUtil.getLoginGuardianId(session);
        RewardDTO rewardInfo = rewardService.getReward(userIdx, rewardIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 구매 완료", rewardInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 리워드 업로드
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertReward(@RequestParam(value = "url", required = false) @NonNull final MultipartFile multipartFile,
                                          @RequestParam(value = "userIdx", required = false) @NonNull final String userIdx,
                                          @RequestParam(value = "description", required = false) final String description
    ) throws IOException {

        RewardDTO rewardDTO = s3Service.insertRewardFile(Long.parseLong(userIdx), description, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "리워드 추가 완료", rewardDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 수정
     */
    @PatchMapping("/update")
    public ResponseEntity<?> updateReward(@RequestBody RewardDTO rewardDTO){
        s3Service.updateReward(rewardDTO);
        SuccessResponse response = new SuccessResponse(true, "reward 수정 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 삭제
     */
    @DeleteMapping("/delete/{rewardIdx}")
    public ResponseEntity<?> deleteReward(@PathVariable("rewardIdx") @NonNull long rewardIdx) throws IOException {

        rewardService.deleteReward(rewardIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 삭제 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
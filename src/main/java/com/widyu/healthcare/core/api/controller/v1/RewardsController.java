package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;
import com.widyu.healthcare.core.domain.service.v1.RewardsService;
import com.widyu.healthcare.core.domain.service.v1.S3Service;
import com.widyu.healthcare.support.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reward")
public class RewardsController {
    private final S3Service s3Service;
    private final RewardsService rewardsService;
    /**
     * 리워드 전체 목록 조회
     */
    @GetMapping("/gurdian/all")
    public ResponseEntity<?> getAllGurdianReward(HttpSession session){

        long userIdx = SessionUtil.getLoginGuardianIdx(session);
        List<RewardResponse> rewardAllInfo = rewardsService.getAllGurdianReward(userIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 조회 완료", rewardAllInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 전체 목록 조회
     */
    @GetMapping("/senior/all")
    public ResponseEntity<?> getAllSeniorReward(HttpSession session){

        long userIdx = SessionUtil.getLoginSeniorIdx(session);
        List<RewardResponse> rewardAllInfo = rewardsService.getAllSeniorReward(userIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 조회 완료", rewardAllInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 구매
     */
    @GetMapping("/buy/{rewardIdx}")
    public ResponseEntity<?> getReward(@PathVariable Long rewardIdx, HttpSession session) throws InsufficientPointsException {
        long userIdx = SessionUtil.getLoginSeniorIdx(session);
        Reward rewardInfo = rewardsService.getReward(userIdx, rewardIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 구매 완료", rewardInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 리워드 업로드
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertReward(@RequestParam(value = "url", required = false) @NonNull final MultipartFile multipartFile,
                                          @RequestParam(value = "userIdx", required = false) @NonNull final String userIdx,
                                          @RequestParam(value = "type", required = false) @NonNull  final RewardType type,
                                          @RequestParam(value = "description", required = false) final String description,
                                          HttpSession session
    ) throws IOException {

        long uploaderIdx = SessionUtil.getLoginGuardianIdx(session);
        Reward reward = s3Service.insertRewardFile(Long.parseLong(userIdx), uploaderIdx, description, type, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "리워드 추가 완료", reward);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 수정
     */
    @PatchMapping("/update")
    // TODO: Reward 도메인 분리 -> .toReward
    public ResponseEntity<?> updateReward(@RequestBody Reward reward) {
        s3Service.updateReward(reward);
        SuccessResponse response = new SuccessResponse(true, "reward 수정 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 삭제
     */
    @DeleteMapping("/delete/{rewardIdx}")
    public ResponseEntity<?> deleteReward(@PathVariable("rewardIdx") @NonNull long rewardIdx) throws IOException {

        rewardsService.deleteReward(rewardIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 삭제 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
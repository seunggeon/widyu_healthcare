package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;
import com.widyu.healthcare.core.domain.service.v1.RewardsService;
import com.widyu.healthcare.core.domain.service.v1.S3Service;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @GetMapping("/guardian/all")
    public ResponseEntity<?> getAllGuardianReward(HttpSession session){

        long userIdx = SessionUtil.getLoginGuardianIdx(session);
        List<RewardResponse> rewardAllInfo = rewardsService.getAllGuardianReward(userIdx);
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
        rewardsService.getReward(userIdx, rewardIdx);
        SuccessResponse response = new SuccessResponse(true, "reward 구매 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * 리워드 업로드
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertReward(@RequestParam(value = "url", required = false) @NonNull final MultipartFile multipartFile,
                                          @RequestParam(value = "type", required = false) @NonNull  final RewardType type,
                                          @RequestParam(value = "description", required = false) final String description,
                                          HttpSession session
    ) throws IOException {

        long uploaderIdx = SessionUtil.getLoginGuardianIdx(session);
        List<Reward> reward = s3Service.insertRewardFile(uploaderIdx, description, type, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "리워드 추가 완료", reward);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 수정 (file)
     */
    @PatchMapping("/update/file")
    // TODO: Reward 도메인 분리 -> .toReward
    public ResponseEntity<?> updateRewardFile(@RequestParam(value = "rewardIdx", required = false) @NonNull final long rewardIdx,
                                          @RequestParam(value = "url", required = false) @NonNull final MultipartFile multipartFile,
                                          HttpSession session) throws IOException {
        s3Service.updateRewardFile(rewardIdx, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "reward 수정 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리워드 수정 (info)
     */
    @PatchMapping("/update/info")
    // TODO: Reward 도메인 분리 -> .toReward
    public ResponseEntity<?> updateRewardInfo(@RequestParam(value = "rewardIdx", required = false) @NonNull final long rewardIdx,
                                          @RequestParam(value = "type", required = false) @NonNull  final RewardType type,
                                          @RequestParam(value = "description", required = false) final String description,
                                          HttpSession session) throws IOException {
        rewardsService.updateRewardInfo(rewardIdx, description, type);
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
package com.widyu.healthcare.core.api.controller.v1;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.domain.service.v1.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {
    private S3Service s3Service;

    /**
     * 사진 업로드 (약 인증)
     */
    @PostMapping("/goal/insert/{goalStatusIdx}")
    public ResponseEntity<?> insertFile(@PathVariable Long goalStatusIdx,
                                     @RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile
    ) throws IOException {

        s3Service.insertGoalFile(goalStatusIdx, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "약 복용 사진 업로드 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사진 삭제 (약 인증)
     */
    @GetMapping("/goal/delete/{goalStatusIdx}")
    public ResponseEntity<?> deleteFile(@PathVariable Long goalStatusIdx) throws IOException {
        s3Service.deleteGoalFile(goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "약 복용 사진 삭제 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

package com.widyu.healthcare.controller;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    /**
     * 사진 업로드 (약 인증)
     */
    @PostMapping("/insert/{goalStatusIdx}")
    public ResponseEntity<?> insertFile(@PathVariable long goalStatusIdx,
                                     @RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile
    ) throws IOException {

        s3Service.insertFile(goalStatusIdx, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "goalStatus file upload 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사진 삭제 (약 인증)
     */
    @GetMapping("/delete/{goalStatusIdx}")
    public ResponseEntity<?> deleteFile(@PathVariable long goalStatusIdx) throws IOException {
        s3Service.deleteFile(goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "goalStatus file delete 완료", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

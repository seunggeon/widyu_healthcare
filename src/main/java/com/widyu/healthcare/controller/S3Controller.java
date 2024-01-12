package com.widyu.healthcare.controller;

import com.widyu.healthcare.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    /**
     * 사진 업로드
     */
    @PostMapping("/insert/{goalIdx}")
    public void insertFile(long goalIdx,
                           @RequestParam(value = "time")LocalDateTime time,
                           @RequestParam(value = "url", required = false) final MultipartFile multipartFile
    ) throws IOException {

        s3Service.insertFile(goalIdx, time, multipartFile);
    }

    /**
     * 사진 삭제
     */
    /*
    @GetMapping("/delete/{userIdx}/{goalIdx}")
    public void deleteFile(@PathVariable long userIdx, long goalIdx){
        s3Service.deleteFile(userIdx, goalIdx);
    }
     */

}

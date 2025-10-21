package com.webproject.controller;

import com.webproject.pojo.Result;
import com.webproject.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController
{
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping("/upload")
    public Result upload(MultipartFile  file) throws Exception
    {
        log.info("文件上传 {}", file.getOriginalFilename());
        //将文件上传到oss
        String url=aliyunOSSOperator.upload(file.getBytes(),file.getOriginalFilename());

        log.info("文件上传到oss中 {}",url);

        return Result.success(url);
    }
}

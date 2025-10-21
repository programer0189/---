package com.webproject.utils;

import com.aliyun.oss.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyunOSSOperator {

    @Value("${aliyun.oss.endpoint:https://oss-cn-beijing.aliyuncs.com}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name:myprojrct-javaweb}")
    private String bucketName;

    @Value("${aliyun.oss.region:cn-beijing}")
    private String region;

    public String upload(byte[] content, String originalFilename) throws Exception {
        // 使用配置文件中的访问凭证
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 获取当前系统日期的字符串,格式为 yyyy/MM
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            // 生成一个新的不重复的文件名
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = dir + "/" + newFileName;

            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));

            return endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + objectName;
        } finally {
            ossClient.shutdown();
        }
    }
}
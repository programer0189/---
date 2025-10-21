package com.webproject.controller;

import com.webproject.pojo.Result;
import com.webproject.pojo.joboption;
import com.webproject.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController
{
    @Autowired
    private ReportService reportService;
    @GetMapping("/empJobData")
    public Result EmJobData()
    {
        log.info("统计数据");

        joboption jobOption=reportService.getEmpJobData();

        return Result.success(jobOption);

    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData()
    {
        log.info("统计性别");
        List<Map<String,Object>> genderlist=reportService.getEmpGenderData();
        return Result.success(genderlist);
    }
    @GetMapping("/locationClick")
    public Result getLocationClick()
    {
        log.info("统计点击");
        List<Map<String,Object>> clicklist=reportService.getLocationClick();
        return Result.success(clicklist);
    }
    
    @GetMapping("/userGenderData")
    public Result getUserGenderData()
    {
        log.info("统计用户性别");
        List<Map<String,Object>> genderList = reportService.getUserGenderData();
        return Result.success(genderList);
    }
    
    @GetMapping("/userCountData")
    public Result getUserCountData()
    {
        log.info("统计用户数量");
        Map<String, Object> countData = reportService.getUserCountData();
        return Result.success(countData);
    }
    
    @GetMapping("/postData")
    public Result getPostData()
    {
        log.info("统计帖子数据");
        Map<String, Object> postData = reportService.getPostData();
        return Result.success(postData);
    }
    
    @GetMapping("/newUserData")
    public Result getNewUserData()
    {
        log.info("统计新增用户数据");
        Map<String, Object> newUserData = reportService.getNewUserData();
        return Result.success(newUserData);
    }
}

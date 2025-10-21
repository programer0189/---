package com.webproject.controller;

import com.webproject.pojo.Result;
import com.webproject.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/logs")
public class SysLogController {

    @Autowired
    private SysLogService log1;

    @GetMapping
    public Result getLogs(@RequestParam Integer page, @RequestParam Integer pageSize) {
        log.info("分页查询日志");
        return Result.success(log1.page(page, pageSize));
    }
}

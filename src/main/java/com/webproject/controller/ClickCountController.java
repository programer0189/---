package com.webproject.controller;


import com.webproject.pojo.ClickCountRequest;
import com.webproject.pojo.Result;
import com.webproject.service.ClickCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/click")
@Slf4j
public class ClickCountController {

    @Autowired
    private ClickCountService clickcountService;

    @PostMapping
    public Result addClick(@RequestBody ClickCountRequest request)
    {
        log.info("点击位置请求: {}", request);
        
        Integer locationId = request.getLocationIdAsInteger();
        if (locationId == null) {
            return Result.error("locationId不能为空或格式不正确");
        }

        clickcountService.addClick(locationId);
        return Result.success();
    }
}

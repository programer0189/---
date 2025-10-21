package com.webproject.controller;

import com.webproject.pojo.LoginInfo;
import com.webproject.pojo.Result;
import com.webproject.pojo.User;
import com.webproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 */
@Slf4j
@RestController
public class LoginController {


    @Autowired
    private UserService userService;
    /**
     * 登录
     */

    @PostMapping("/user/login")
    public Result login(@RequestBody User user) {
        log.info("用户登录：{}", user);

        LoginInfo loginInfo = userService.login(user);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误");
    }
}





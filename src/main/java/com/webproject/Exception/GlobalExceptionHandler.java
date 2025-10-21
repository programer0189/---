package com.webproject.Exception;

import com.webproject.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{   @ExceptionHandler
    public Result error(Exception e)
    {
        log.info("服务出错",e);
        return Result.error("服务器异常");
    }

    @ExceptionHandler
    public Result error(DuplicateKeyException e)
    {
        log.info("重复数据",e);
        String message=e.getMessage();
        int i=message.indexOf("Duplicate entry");
        String errMsg=message.substring(i);
        String arr[]=errMsg.split(" ");
        return Result.error(arr[2]+"已存在");
    }
}

package com.webproject.service;

import com.webproject.pojo.PageResult;
import com.webproject.pojo.sysLog;

public interface SysLogService
{
    PageResult<sysLog> page(Integer page, Integer pageSize);
}

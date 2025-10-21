package com.webproject.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.webproject.mapper.SysLogMapper;
import com.webproject.pojo.PageResult;
import com.webproject.pojo.sysLog;
import com.webproject.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService
{
    @Autowired
    private SysLogMapper logMapper;

    @Override
    public PageResult<sysLog> page(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Page<sysLog> p = (Page<sysLog>) logMapper.findAll();
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}

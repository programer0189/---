package com.webproject.service.impl;

import com.webproject.mapper.ClickCountMapper;
import com.webproject.mapper.EmpMapper;
import com.webproject.mapper.UserMapper;
import com.webproject.mapper.PostMapper;
import com.webproject.pojo.joboption;
import com.webproject.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportSevriceimpl implements ReportService
{

    @Autowired
    private EmpMapper empMapper;
    @Autowired

    private ClickCountMapper clickCountMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;
    
    @Override

    public joboption getEmpJobData() {
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        List<Object> joblist = list.stream()
                .map(dataMap -> dataMap.get("pos"))
                .toList();
        List<Object> datalist = list.stream()
                .map(dataMap -> dataMap.get("num"))
                .toList();
        return new joboption(joblist, datalist);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData()
    {
        return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map<String, Object>> getLocationClick()
    {
       return clickCountMapper.countClick();
    }
    
    @Override
    public List<Map<String, Object>> getUserGenderData()
    {
        return userMapper.countUserGenderData();
    }
    
    @Override
    public Map<String, Object> getUserCountData()
    {
        return userMapper.getUserCountData();
    }
    
    @Override
    public Map<String, Object> getPostData()
    {
        return postMapper.getPostData();
    }
    
    @Override
    public Map<String, Object> getNewUserData()
    {
        return userMapper.getNewUserData();
    }
}

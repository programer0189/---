package com.webproject.service;

import com.webproject.pojo.joboption;

import java.util.List;
import java.util.Map;

public interface ReportService
{

    joboption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    List<Map<String, Object>> getLocationClick();
    
    List<Map<String, Object>> getUserGenderData();
    
    Map<String, Object> getUserCountData();
    
    Map<String, Object> getPostData();
    
    Map<String, Object> getNewUserData();
}

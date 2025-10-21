package com.webproject.service;

import com.webproject.pojo.Emp;
import com.webproject.pojo.EmpQueryParam;
import com.webproject.pojo.LoginInfo;
import com.webproject.pojo.PageResult;

import java.util.List;

public interface EmpService {
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);
    LoginInfo login(Emp emp);
}

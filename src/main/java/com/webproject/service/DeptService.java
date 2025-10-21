package com.webproject.service;

import com.webproject.pojo.Dept;


import java.util.List;

public interface DeptService
{
    List<Dept> findAll();

    void deleteById(Integer id);

     void add(Dept dept);

    Dept getBy(Integer deptId);

    void updateDep(Dept dep);
}

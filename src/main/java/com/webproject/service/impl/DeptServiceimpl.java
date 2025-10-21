package com.webproject.service.impl;

import com.webproject.mapper.DeptMapper;
import com.webproject.pojo.Dept;
import com.webproject.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceimpl implements DeptService
{

    @Autowired
    private DeptMapper deptmapper;
    @Override
    public List<Dept> findAll()
    {
       return deptmapper.findAll();
    }

    @Override
    public void deleteById(Integer id)
    {
        deptmapper.deleteById(id);

    }

    @Override
    public void add(Dept dept)
    {

        //1:先补全属性
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        //2:调用mapper方法
        deptmapper.add(dept);

    }

    @Override
    public Dept getBy(Integer deptId) {

       return deptmapper.getById(deptId);
    }

    @Override
    public void updateDep(Dept dep)
    {
        dep.setUpdateTime(LocalDateTime.now());
        deptmapper.updateDept(dep);
    }
}

package com.webproject.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.webproject.mapper.EmpExprMapper;
import com.webproject.mapper.EmpMapper;
import com.webproject.pojo.*;
import com.webproject.service.EmpService;
import com.webproject.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class EmpServiceimpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize)
//    {
//        Long total=empMapper.count();
//        Integer start=(page-1)*pageSize;
//        //传递的是起始索引
//        List<Emp> Rows=empMapper.list(start,pageSize);
//
//        return new PageResult<Emp>(total,Rows);
//
//
//    }
    /*
    pagehelper分页插件
     */
    public PageResult<Emp> page(EmpQueryParam empQueryParam)
    {
        //1.设置起始索引和每页显示的条数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());


        //2.查询总记录数
       List<Emp> empList = empMapper.list(empQueryParam);
       //强制转换为 Page
       Page<Emp> p=(Page<Emp>) empList;





        //3.封装结果
        return new PageResult<Emp>(p.getTotal(),p.getResult());
    }

    //新增员工
    @Transactional
    @Override
    public void save(Emp emp)
    {
        //1.保存员工的基本信息
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        //2.保存员工对应的工作经历
        List<EmpExpr> exprList = emp.getExprList();
       if(!CollectionUtils.isEmpty(exprList))
       {
           for (EmpExpr empExpr : exprList)
           {
               empExpr.setEmpId(emp.getId());
           }
           empExprMapper.insertBatch(exprList);
       }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer>ids)
    {
        //1.删除员工的基本信息
        empMapper.deleteByIds(ids);



        //2.删除员工对应的工作经历
        empExprMapper.deleteByEmpIds(ids);

    }

    @Override
    public Emp getInfo(Integer id)
    {


        return empMapper.getById(id);
    }
//修改员工
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(Emp emp)
    {
        //1.根据id修改员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);



        //2.修改员工对应的工作经历,先根据id删除原有的的经历再增加新的
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        List<EmpExpr> exprList=emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList))
        {
            for (EmpExpr empExpr : exprList)
            {
                empExpr.setEmpId(emp.getId());
                empExprMapper.insertBatch(exprList);
            }
        }

    }
    @Override
    public LoginInfo login(Emp emp) {
        //1. 调用mapper接口, 根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);

        //2. 判断: 判断是否存在这个员工, 如果存在, 组装登录成功信息
        if(e != null){
            log.info("登录成功, 员工信息: {}", e);
            //生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateToken(claims);

            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }

        //3. 不存在, 返回null
        return null;
    }

}

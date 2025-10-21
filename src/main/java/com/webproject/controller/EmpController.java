package com.webproject.controller;

import com.webproject.pojo.Emp;
import com.webproject.pojo.EmpQueryParam;
import com.webproject.pojo.PageResult;
import com.webproject.pojo.Result;
import com.webproject.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

//员工管理的controller
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController
{
    @Autowired
    private EmpService empservice;
    @GetMapping
//    public Result page(@RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam(defaultValue = "10") Integer pageSize,
//                       String name, Integer gender,
//                       @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate begin,
//                       @DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate end)
//    {
//        log.info("分页查询 {} {} {} {} {} {}",page,pageSize,name,gender,begin,end);
//        PageResult<Emp> pageResult=empservice.page(page,pageSize,name,gender,begin,end);
//
//        return Result.success(pageResult);
//    }


    public Result page(EmpQueryParam empQueryParam)
    {
        log.info("分页查询 {}",empQueryParam);
        PageResult<Emp> pageResult=empservice.page(empQueryParam);

        return Result.success(pageResult);
    }
    /*
    新增员工
     */
    @PostMapping
    public Result save(@RequestBody Emp emp)
    {
        log.info("员工保存 {}",emp);
        empservice.save(emp);

        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Integer> ids)
    {
        log.info("删除员工 {}", ids);
        empservice.delete(ids);
        return Result.success();

    }


//    根据id查询员工信息
  @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id)
    {
        log.info("查询{}的员工",id);
        Emp emp=empservice.getInfo(id);
        return Result.success(emp);
    }
    //修改员工
    @PutMapping
    public Result update(@RequestBody Emp emp)
    {
        log.info("修改员工{}",emp);
        empservice.update(emp);
        return Result.success();
    }
}

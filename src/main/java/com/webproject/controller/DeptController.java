package com.webproject.controller;

import com.webproject.pojo.Dept;
import com.webproject.pojo.Result;
import com.webproject.service.DeptService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class DeptController
{
    @Autowired
    private DeptService deptservice;
//    通过@RequestMapping注解，将一个方法映射为当前请求地址，并返回一个数据，method 指定请求方式
//    或者 @GetMapping("/depts")
    @RequestMapping(value = "/depts",method = RequestMethod.GET)
    public Result list()
    {
        //System.out.println("查询全部数据");
        log.info("查询全部数据");
        List<Dept> deptslist= deptservice.findAll();

        return Result.success(deptslist);

    }

    //根据http协议删除部门
//    @DeleteMapping("/depts")
//    public Result delete(HttpServletRequest request)
//    {
//       String idstr= request.getParameter("id");
//       int id=Integer.parseInt(idstr);//转换为int型
//       System.out.println("根据ID删除部门： "+id);
//
//       return Result.success();
//
//    }

    //使用@RequestParam注解，将请求参数映射为方法参数
//    @DeleteMapping("/depts")
//    public Result delete(@RequestParam("id") Integer deptid)
//    {
//        System.out.println("根据ID删除部门： "+deptid);
//        return Result.success();
//    }



    //当前端数据传递回来的参数与方法参数名称一致时，可以省略@RequestParam注解
    @DeleteMapping("/depts")
    public Result delete(Integer id)
    {
        System.out.println("根据ID删除部门： "+id);
        //log.info("删除部门： {}",id);
        deptservice.deleteById(id);
        return Result.success();
    }

    //使用@RequestBody注解，将请求体中json的数据映射为方法参数,封装到一个对象中属性值要对应
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept)
    {
        System.out.println("添加部门： "+dept);
        //log.info("添加部门： {}",dept);
        deptservice.add(dept);

        return Result.success();
    }

    //slfj4 日志框架
    @GetMapping("/depts/{id}")
    public Result getInfo(@PathVariable("id") Integer deptId)
    {
        log.info("查询部门ID： {}",deptId);
        //System.out.println("根据Id查询部门"+deptId);

        Dept dept=deptservice.getBy(deptId);

        return Result.success(dept);
    }

    @PutMapping("/depts")
    public Result updateDept(@RequestBody Dept dep)
    {
        log.info("修改部门： {}",dep);
        //System.out.println("修改部门" +dep);
        deptservice.updateDep(dep);

        return Result.success();
    }

}

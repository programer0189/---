package com.webproject.controller;


import com.webproject.pojo.*;
import com.webproject.service.schoolPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/maps")
public class positionController
{

    @Autowired
    private schoolPosition schoolposition;
//    @GetMapping
//    //查询所有地图
//    public Result findAll()
//    {
//        log.info("查询所有地图");
//        List<Map> schoolMap=schoolposition.findAll();
//        return Result.success(schoolMap);
//    }

    //分页查询

    @GetMapping
    public Result page(MapQueryParam mapQueryParam)
    {
        log.info("分页查询 {}",mapQueryParam);
        mapPageResult<Map> pageResult=schoolposition.page(mapQueryParam);

        return Result.success(pageResult);
    }
    //根据id查询地图信息
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id)
    {
        log.info("id查询地图信息 {}", id);
        Map map=schoolposition.getInfo(id);

        return Result.success(map);
    }



    //修改地图信息
    @PutMapping
    public  Result updateMap(@RequestBody Map map)
    {
        log.info("修改地图信息 {}", map);
        schoolposition.update(map);
        return Result.success();

    }

    //插入地图信息
    @PostMapping
    public Result save(@RequestBody Map map)
    {
        //1插入地图信息
        log.info("插入地图信息 {}", map);
        schoolposition.save(map);


        return Result.success();
    }

//    //根据id删除单一地图
//    @DeleteMapping
//    public Result Delete(Integer id)
//    {
//        log.info("删除序号为{}的地图",id);
//                schoolposition.DeleteById(id);
//        return Result.success();
//    }


    //批量删除
    @DeleteMapping
    public Result Deletes(@RequestParam("ids") List<Integer> ids)
    {
        log.info("批量删除地图 {}", ids);
        schoolposition.DeleteByIds(ids);
        return Result.success();
    }

}

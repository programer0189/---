package com.webproject.mapper;


import com.webproject.pojo.Map;
import com.webproject.pojo.MapQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface positionMapper
{



    @Select("select * from location")
    List<Map> findAll();


    //更新地图信息表
    void updateById(Map map);

    //插入地图信息表
    void saveMap(Map map);

    //通过id删除地图信息
    void DeleteById(Integer id);

    //通过id批量删除地图信息
    void DeleteByIds(List<Integer> ids);

    List<Map> pageList(MapQueryParam mapQueryParam);

    //通过id查询地图信息
    Map getInfo(Integer id);
}



package com.webproject.service;



import com.webproject.pojo.Map;
import com.webproject.pojo.MapQueryParam;
import com.webproject.pojo.mapPageResult;

import java.util.List;

public interface schoolPosition
{
    List<Map> findAll();

    void update(Map map);

    void save(Map map);

    void DeleteById(Integer id);

    //批量删除
    void DeleteByIds(List<Integer> ids);

    mapPageResult<Map> page(MapQueryParam mapQueryParam);

    Map getInfo(Integer id);
}

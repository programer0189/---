package com.webproject.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.webproject.mapper.positionMapper;
import com.webproject.pojo.Map;
import com.webproject.pojo.MapQueryParam;
import com.webproject.pojo.mapPageResult;
import com.webproject.service.schoolPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class schoolPositionimpl implements schoolPosition
{
    @Autowired
    private positionMapper positionmapper;
    @Override
    //查询所有地图
    public List<Map> findAll()
    {

        return positionmapper.findAll();

    }
//修改
    @Override
    public void update(Map map)
    {
        map.setUpdateTime(LocalDateTime.now());
        positionmapper.updateById(map);

    }

    //保存
    @Override
    public void save(Map map) {
        positionmapper.saveMap(map);
    }

    //单一删除
    @Override
    public void DeleteById(Integer id)
    {
        positionmapper.DeleteById(id);

    }

    //批量删除
    @Override
    public void DeleteByIds(List<Integer> ids)
    {
        positionmapper.DeleteByIds(ids);

    }

    @Override
    public mapPageResult<Map> page(MapQueryParam mapQueryParam)
    {
        PageHelper.startPage(mapQueryParam.getPage(), mapQueryParam.getPageSize());
        List<Map> list = positionmapper.pageList(mapQueryParam);
        Page<Map> page = (Page<Map>) list;
        return new mapPageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Map getInfo(Integer id)
    {
        return positionmapper.getInfo(id);
    }


}


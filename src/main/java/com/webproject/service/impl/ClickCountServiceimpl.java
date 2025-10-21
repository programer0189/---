package com.webproject.service.impl;


import com.webproject.mapper.ClickCountMapper;
import com.webproject.pojo.ClickCount;
import com.webproject.service.ClickCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClickCountServiceimpl implements ClickCountService
{
    @Autowired
    private ClickCountMapper clickCountMapper;
    @Override
    public void addClick(Integer locationId)
    {
        ClickCount click = new ClickCount();
        click.setLocationId(locationId);
        click.setClickTime(LocalDateTime.now());
        clickCountMapper.insert(click);



    }
}

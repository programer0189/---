package com.webproject.mapper;


import com.webproject.pojo.ClickCount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClickCountMapper
{

    @Insert("insert into click_count(id,location_id,click_time) values(#{id},#{locationId},#{clickTime})")
    void insert(ClickCount click);
    @Select("""
        SELECT
            l.name AS locationName,
            COUNT(*) AS clickCount
        FROM
            click_count c
        JOIN
            location l ON c.location_id = l.id
        WHERE
            l.id NOT IN (41, 42)
        GROUP BY
            c.location_id
        ORDER BY
            clickCount DESC
        LIMIT 5
    """)
    List<Map<String, Object>> countClick();


}

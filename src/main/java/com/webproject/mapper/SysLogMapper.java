package com.webproject.mapper;

import com.webproject.pojo.sysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysLogMapper {

    @Select("SELECT * FROM sys_log ORDER BY create_time")
    List<sysLog> findAll();     // 如果分页用 PageHelper，可以写 pageList()

    @Insert("INSERT INTO sys_log(level, logger, message, create_time) VALUES (#{level},#{logger},#{message},#{createTime})")
    void insert(sysLog log);
}

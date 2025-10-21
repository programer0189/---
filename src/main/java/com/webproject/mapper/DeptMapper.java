package com.webproject.mapper;

import com.webproject.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper
{
//    查询所有的部门信息
    /*
    当数据库中的字段名和实体类属性名不一致时，为数据库的字段起别名，在实体类中将别名映射为属性名
     */
//    @Select("select id,name,create_time createTime ,update_time updateTime from dept order by update_time desc")
    @Select("select id,name,create_time ,update_time  from dept order by update_time desc")
    List<Dept> findAll();

    @Delete("Delete from dept where  id=#{id}")
    void deleteById(Integer id);

    @Insert("Insert into dept( name, create_time, update_time) VALUES(#{name},#{createTime},#{updateTime})")
    void add(Dept dept);
    @Select("select id,name,create_time,update_time from dept where id=#{deptId}")
    Dept getById(Integer deptId);

    @Update("Update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    void updateDept(Dept dep);
}

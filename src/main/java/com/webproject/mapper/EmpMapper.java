package com.webproject.mapper;


import com.webproject.pojo.Emp;
import com.webproject.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/*
    操作员工基本信息的Mapper接口
 */
@Mapper
public interface EmpMapper
{
//    @Select("SELECT count(*)from emp e left join dept d on e.dept_id=d.id;")
//    public Long count();
//    @Select("SELECT e.*,d.name deptName from emp e left join dept d on e.dept_id=d.id " +
//            "order by e.update_time desc limit #{start},#{pages}")
//    public List<Emp> list(Integer start, Integer pages);
    //字符串拼接函数concat
//@Select("SELECT e.*, d.name deptName " +
//        "FROM emp e LEFT JOIN dept d ON e.dept_id = d.id " +
//        "WHERE e.name LIKE CONCAT('%', #{name}, '%') " +
//        "  AND e.gender = #{gender} " +
//        "  AND e.entry_date BETWEEN #{begin} AND #{end} " +
//        "ORDER BY e.update_time DESC")
List<Emp> list(EmpQueryParam empQueryParam);
    //主键返回
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) VALUES(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    //批量删除
    void deleteByIds(List<Integer> ids);
//根据id查询员工信息和工作经历
    Emp getById(Integer id);

//根据id更新员工基本信息
    void updateById(Emp emp);



    List<Map<String,Object>> countEmpJobData();

    List<Map<String, Object>> countEmpGenderData();

    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}

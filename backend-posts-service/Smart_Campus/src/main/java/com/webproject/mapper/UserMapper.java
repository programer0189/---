package com.webproject.mapper;

import com.webproject.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    
    // 登录查询（保留原有）
    User selectByUsernameAndPassword(User user);
    
    // 新增用户
    void insert(User user);
    
    // 根据ID查询用户
    User selectById(Integer id);
    
    // 查询所有用户
    List<User> selectAll();
    
    // 更新用户信息
    void update(User user);
    
    // 删除用户
    void deleteById(Integer id);
    
    // 根据用户名查询用户
    User selectByUsername(String username);
    
    // 根据手机号查询用户
    User selectByPhone(String phone);
    
    // 根据角色查询用户
    List<User> selectByRole(String role);
    
    // 统计用户性别数据
    List<Map<String, Object>> countUserGenderData();
    
    // 获取用户数量统计
    Map<String, Object> getUserCountData();
    
    // 获取新增用户统计
    Map<String, Object> getNewUserData();
}

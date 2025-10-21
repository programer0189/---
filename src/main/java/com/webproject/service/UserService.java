package com.webproject.service;

import com.webproject.pojo.LoginInfo;
import com.webproject.pojo.User;

import java.util.List;

public interface UserService {
    
    // 登录方法（保留原有）
    LoginInfo login(User user);
    
    // 新增用户
    void addUser(User user);
    
    // 根据ID查询用户
    User getUserById(Integer id);
    
    // 查询所有用户
    List<User> getAllUsers();
    
    // 更新用户信息
    void updateUser(User user);
    
    // 删除用户
    void deleteUser(Integer id);
    
    // 根据用户名查询用户
    User getUserByUsername(String username);
    
    // 根据手机号查询用户
    User getUserByPhone(String phone);
    
    // 根据角色查询用户
    List<User> getUsersByRole(String role);
}

package com.webproject.service.impl;

import com.webproject.mapper.UserMapper;
import com.webproject.pojo.LoginInfo;
import com.webproject.pojo.User;
import com.webproject.service.UserService;
import com.webproject.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginInfo login(User user) {
        //1. 根据用户名和密码查询
        User u = userMapper.selectByUsernameAndPassword(user);
        //2. 判断是否存在用户
        if (u != null) {
            log.info("用户登录成功：{}", u);
            // 生成JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            claims.put("role", u.getRole());
            String jwt = JwtUtils.generateToken(claims);

            return new LoginInfo(u.getId(),u.getUsername(),u.getRole(),jwt);
        }
        return null;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        // 设置创建时间和更新时间
        user.setCreateTime(LocalDate.now());
        user.setUpdateTime(LocalDate.now());
        
        // 设置默认角色（如果没有指定）
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        
        userMapper.insert(user);
        log.info("新增用户：{}", user.getUsername());
    }

    @Override
    public User getUserById(Integer id) {
        log.info("根据ID查询用户：{}", id);
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("查询所有用户");
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 设置更新时间
        user.setUpdateTime(LocalDate.now());
        
        userMapper.update(user);
        log.info("更新用户信息：{}", user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
        log.info("删除用户：{}", id);
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("根据用户名查询用户：{}", username);
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByPhone(String phone) {
        log.info("根据手机号查询用户：{}", phone);
        return userMapper.selectByPhone(phone);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        log.info("根据角色查询用户：{}", role);
        return userMapper.selectByRole(role);
    }
}

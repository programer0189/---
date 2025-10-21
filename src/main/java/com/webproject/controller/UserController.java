package com.webproject.controller;

import com.webproject.pojo.Result;
import com.webproject.pojo.User;
import com.webproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增用户
     */
    @PostMapping
    public Result addUser(@RequestBody User user) {
        log.info("新增用户：{}", user);
        
        // 检查用户名是否已存在
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            User existingPhone = userService.getUserByPhone(user.getPhone());
            if (existingPhone != null) {
                return Result.error("手机号已存在");
            }
        }
        
        userService.addUser(user);
        return Result.success();
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        log.info("根据ID查询用户：{}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 查询所有用户
     */
    @GetMapping
    public Result getAllUsers() {
        log.info("查询所有用户");
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }
    
    /**
     * 分页查询用户
     */
    @GetMapping("/page")
        public Result getUsersWithPage(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询用户：第{}页，每页{}条", page, pageSize);
        PageHelper.startPage(page, pageSize);
        List<User> users = userService.getAllUsers();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return Result.success(pageInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Integer id, @RequestBody User user) {
        log.info("更新用户信息：{}", user);
        
        // 设置ID
        user.setId(id);
        
        // 检查用户名是否已被其他用户使用
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            return Result.error("用户名已被其他用户使用");
        }
        
        // 检查手机号是否已被其他用户使用
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            User existingPhone = userService.getUserByPhone(user.getPhone());
            if (existingPhone != null && !existingPhone.getId().equals(id)) {
                return Result.error("手机号已被其他用户使用");
            }
        }
        
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        log.info("删除用户：{}", id);
        
        // 检查用户是否存在
        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public Result getUserByUsername(@PathVariable String username) {
        log.info("根据用户名查询用户：{}", username);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 根据手机号查询用户
     */
    @GetMapping("/phone/{phone}")
    public Result getUserByPhone(@PathVariable String phone) {
        log.info("根据手机号查询用户：{}", phone);
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 根据角色查询用户
     */
    @GetMapping("/role/{role}")
    public Result getUsersByRole(@PathVariable String role) {
        log.info("根据角色查询用户：{}", role);
        List<User> users = userService.getUsersByRole(role);
        return Result.success(users);
    }
}

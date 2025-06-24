package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success("获取成功", users);
    }

    /**
     * 根据ID获取用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return Result.success("获取成功", user);
    }

    /**
     * 添加用户
     */
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        if (result > 0) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        int result = userService.updateUser(user);
        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable int id) {
        int result = userService.deleteUser(id);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 根据状态获取用户
     */
    @GetMapping("/by-status")
    public Result<List<User>> getUsersByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "false") boolean isDESC,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<User> users = userService.getUsersByStatus(status, isDESC, order, limit, offset);
        return Result.success("获取成功", users);
    }

    /**
     * 根据用户名获取用户
     */
    @GetMapping("/by-username")
    public Result<List<User>> getUsersByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "false") boolean isDESC,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<User> users = userService.getUsersByUsername(username, isDESC, order, limit, offset);
        return Result.success("获取成功", users);
    }

    /**
     * 根据用户角色获取用户
     */
    @GetMapping("/by-role")
    public Result<List<User>> getUsersByRole(
            @RequestParam String role,
            @RequestParam(defaultValue = "false") boolean isDESC,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<User> users = userService.getUsersByRole(role, isDESC, order, limit, offset);
        return Result.success("获取成功", users);
    }
}

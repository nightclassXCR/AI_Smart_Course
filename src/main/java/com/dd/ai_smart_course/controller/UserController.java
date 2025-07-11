package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.UserDTO;
import com.dd.ai_smart_course.dto.request.SearchRequest;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.impl.UserImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserImpl userService;

    @PostMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{id}")
    public Result<User> getUserById(@PathVariable int id){
        log.info("get a request: get user by userID: {}", id);
        return Result.success(userService.getUserById(id));
    }

    @PostMapping("/add")
    public Result<Integer> addUser(@RequestBody User user) throws BusinessException {
        log.info("get a request: add a user");
        try {
            Integer data = userService.addUser(user);
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result<Integer> updateUser(@RequestBody User user) {
        log.info("get a request: update a user");
        try {
            Integer data = userService.updateUser(user);
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public Result<Integer> deleteUser(@PathVariable int id){
        log.info("get a request: delete a user");
        Integer data = userService.deleteUser(id);
        return Result.success(data);
    }

    //根据状态获取用户
    @PostMapping("/getByStatus")
    public Result<List<User>> getUsersByStatus(@RequestBody SearchRequest request){
        String status = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        List<User> data = userService.getUsersByStatus(status, isDESC, order, limit, offset);
        return Result.success(data);
    }

    //根据用户名获取用户
    @PostMapping("/getByUsername")
    public Result<List<User>> getUsersByUsername(@RequestBody SearchRequest request){
        String username = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        List<User> data = userService.getUsersByUsername(username, isDESC, order, limit, offset);
        return Result.success(data);
    }

    //根据用户角色获取用户
    @PostMapping("/getByRole")
    public Result<List<User>>  getUsersByRole(@RequestBody SearchRequest request){
        String role = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        List<User> data = userService.getUsersByRole(role, isDESC, order, limit, offset);
        return Result.success(data);
    }

    //修改用户角色
    @PostMapping("/modify/Role")
    public Result<Boolean> modifyUserRole(@RequestBody UserDTO user) throws BusinessException {
        log.info("get a request: modify user role");
        try {
            boolean data = userService.updateUserRole(user.getId(), user.getRole());
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }

    //修改用户状态
    @PostMapping("/modify/Status")
    public Result<Boolean> modifyUserStatus(@RequestBody UserDTO user) throws BusinessException {
        log.info("get a request: modify user status");
        try {
            boolean data = userService.updateUserStatus(user.getId(), user.getStatus());
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }

    //修改用户密码
    @PostMapping("/modify/Password")
    public Result<Boolean> modifyUserPassword(@RequestBody UserDTO user) throws BusinessException {
        log.info("get a request: modify user password");
        try {
            boolean data = userService.updateUserPassword(user.getId(), user.getPassword());
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }

    //修改用户名
    @PostMapping("/modify/Username")
    public Result<Boolean> modifyUsername(@RequestBody UserDTO user) throws BusinessException {
        log.info("get a request: modify user username");
        try {
            boolean data = userService.updateUsername(user.getId(), user.getUsername());
            return Result.success(data);
        }catch (BusinessException e){
            return Result.error(e.getMessage());
        }
    }
}

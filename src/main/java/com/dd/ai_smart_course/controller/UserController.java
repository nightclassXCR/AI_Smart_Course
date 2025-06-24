package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.service.dto.request.SearchRequest;
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
    User getUserById(@PathVariable int id){
        log.info("get a request: get user by userID: {}", id);
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    int addUser(@RequestBody User user) throws BusinessException {
        log.info("get a request: add a user");
        return userService.addUser(user);
    }

    @PostMapping("/update")
    int updateUser(@RequestBody User user) throws BusinessException{
        log.info("get a request: update a user");
        return userService.updateUser(user);
    }

    @GetMapping("/delete/{id}")
    int deleteUser(@PathVariable int id){
        log.info("get a request: delete a user");
        return userService.deleteUser(id);
    }

    //根据状态获取用户
    @GetMapping
    List<User> getUsersByStatus(@RequestBody SearchRequest request){
        String status = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        return userService.getUsersByStatus(status, isDESC, order, limit, offset);
    }

    //根据用户名获取用户
    List<User> getUsersByUsername(@RequestBody SearchRequest request){
        String username = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        return userService.getUsersByStatus(username, isDESC, order, limit, offset);
    }

    //根据用户角色获取用户
    List<User> getUsersByRole(@RequestBody SearchRequest request){
        String role = request.getCompareParam();
        boolean isDESC = request.isDESC();
        String order = request.getOrder();
        Integer limit = request.getLimit();
        Integer offset = request.getOffset();
        return userService.getUsersByStatus(role, isDESC, order, limit, offset);
    }
}

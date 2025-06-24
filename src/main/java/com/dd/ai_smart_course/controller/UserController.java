package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.impl.UserImpl;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/{id}")
    User getUserById(@PathVariable int id){
        log.info("get user by userID: {}", id);
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    int addUser(@RequestBody User user){

        return userService.addUser(user);
    }
//    int updateUser(User user);
//    int deleteUser(int id);
//
//    //根据状态获取用户
//    List<User> getUsersByStatus(String status, boolean isDESC, String order, int limit, int offset);
//
//    //根据用户名获取用户
//    List<User> getUsersByUsername(String username, boolean isDESC, String order, int limit, int offset);
//
//    //根据用户角色获取用户
//    List<User> getUsersByRole(String role, boolean isDESC, String order, int limit, int offset);
}

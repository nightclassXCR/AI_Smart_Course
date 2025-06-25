package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(int id);

    //根据状态获取用户
    List<User> getUsersByStatus(String status, boolean isDESC, String order, Integer limit, Integer offset);

    //根据用户名获取用户
    List<User> getUsersByUsername(String username, boolean isDESC, String order, Integer limit, Integer offset);

    //根据用户角色获取用户
    List<User> getUsersByRole(String role, boolean isDESC, String order, Integer limit, Integer offset);
}
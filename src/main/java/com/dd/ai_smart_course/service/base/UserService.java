package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.exception.BusinessException;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(int id);

    //更新用户状态
    boolean updateUserStatus(int userId, String status);

    //更新用户角色
    boolean updateUserRole(int userId, String role);

    //更新用户密码
    boolean updateUserPassword(int userId, String password);

    //更新用户名称
    boolean updateUsername(int userId, String username);

    //根据状态获取用户
    List<User> getUsersByStatus(String status, boolean isDESC, String order, Integer limit, Integer offset);

    //根据用户名获取用户
    List<User> getUsersByUsername(String username, boolean isDESC, String order, Integer limit, Integer offset);

    //根据用户角色获取用户
    List<User> getUsersByRole(String role, boolean isDESC, String order, Integer limit, Integer offset);

    //根据用户ID列表批量获取用户
    List<User> getUsersByIds(List<Integer> userIds);

    //检验关键数据是否非法
    //若姓名、邮箱和电话非法，则抛出对应异常
    //针对add，update
    void checkFactor(User user) throws BusinessException;

    //根据userId检查用户是否存在
    //针对update
    void checkUserExists(int userId) throws BusinessException;

}
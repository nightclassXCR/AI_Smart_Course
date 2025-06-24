package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import com.dd.ai_smart_course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class UserImpl implements UserService {

    //User类的role属性常量
    private static final String USER_ROLE_ADMIN = "ROLE_ADMIN";
    private static final String USER_ROLE_USER = "ROLE_USER";
    public static final List<String> ALLOWED_USER_ROLE = Arrays.asList(USER_ROLE_ADMIN, USER_ROLE_USER);

    //User类的status属性常量
    private static final String USER_STATUS_NORMAL = "NORMAL";
    private static final String USER_STATUS_BANNED = "BANNED";
    public static final List<String> ALLOWED_USER_STATUS = Arrays.asList(USER_STATUS_NORMAL, USER_STATUS_BANNED);

    //SQL片段（如列名、表名、排序方向等）
    //排序方式
    private static final String SQL_DESCENDING = "DESC";
    private static final String SQL_ASCENDING = "ASC";

    //列名
    private static final String SQL_USER_USERNAME = "username";
    private static final String SQL_USER_ROLE = "role";
    private static final String SQL_USER_CREATED_AT = "created_at";
    private static final String SQL_USER_LAST_ACTIVITY_AT = "last_activity_at";
    private static final String SQL_USER_EMAIL = "email";
    private static final String SQL_USER_PHONE_NUMBER = "phone_number";
    private static final String SQL_USER_STATUS = "status";
    public static final List<String> ALLOWED_USER_COLUMN_NAME = Arrays.asList(SQL_USER_USERNAME, SQL_USER_ROLE, SQL_USER_CREATED_AT,
            SQL_USER_LAST_ACTIVITY_AT, SQL_USER_EMAIL, SQL_USER_PHONE_NUMBER, SQL_USER_STATUS);


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    //根据状态获取用户
    @Override
    public List<User> getUsersByStatus(String status, boolean isDESC, String order, int limit, int offset){
        if(!ALLOWED_USER_STATUS.contains(status) || !ALLOWED_USER_COLUMN_NAME.contains(order)){
            return null;
        }

        if(isDESC){
            return userMapper.getUsersByAllParam(SQL_USER_STATUS, status, order, SQL_DESCENDING, limit, offset);
        }else {
            return userMapper.getUsersByAllParam(SQL_USER_STATUS, status, order, SQL_ASCENDING, limit, offset);
        }

    }

    //根据用户名获取用户
    @Override
    public List<User> getUsersByUsername(String username, boolean isDESC, String order, int limit, int offset){
        if(!ALLOWED_USER_COLUMN_NAME.contains(order)){
            return null;
        }

        if(isDESC){
            return userMapper.getUsersByAllParam(SQL_USER_USERNAME, username, order, SQL_DESCENDING, limit, offset);
        }else {
            return userMapper.getUsersByAllParam(SQL_USER_USERNAME, username, order, SQL_ASCENDING, limit, offset);
        }
    }

    //根据用户角色获取用户
    @Override
    public List<User> getUsersByRole(String role, boolean isDESC, String order, int limit, int offset){
        if(!ALLOWED_USER_ROLE.contains(role) || !ALLOWED_USER_COLUMN_NAME.contains(order)){
            return null;
        }

        if(isDESC){
            return userMapper.getUsersByAllParam(SQL_USER_ROLE, role, order, SQL_DESCENDING, limit, offset);
        }else {
            return userMapper.getUsersByAllParam(SQL_USER_ROLE, role, order, SQL_ASCENDING, limit, offset);
        }
    }

    @Override
    public int addUser(User user) {

        try {
            return userMapper.addUser(user);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return userMapper.deleteUser(id);
    }

    //检验是否能进行更改和增加的操作


    //检验用户名称唯一性
    public boolean isNameUnique(String name){
        //调用mapper
        Integer db_id = userMapper.getIDByName(name);
        if(db_id==null){
            //用户不存在，凭证唯一
            return true;
        }else {
            //用户存在，凭证不唯一
            return false;
        }
    }

    //检验电话号码唯一性
    public boolean isPhoneNumberUnique(String phoneNumber){
        //调用mapper
        Integer db_id = userMapper.getIDByPhoneNumber(phoneNumber);
        if(db_id==null){
            //用户不存在，凭证唯一
            return true;
        }else {
            //用户存在，凭证不唯一
            return false;
        }
    }

    //检验邮箱号码唯一性
    public boolean isEmailUnique(String email){
        //调用mapper
        Integer db_id = userMapper.getIDByEmail(email);
        if(db_id==null){
            //用户不存在，凭证唯一
            return true;
        }else {
            //用户存在，凭证不唯一
            return false;
        }
    }
}
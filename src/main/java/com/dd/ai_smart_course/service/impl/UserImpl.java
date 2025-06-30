package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.UserDTO;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.exception.errorcode.ErrorCode;
import com.dd.ai_smart_course.mapper.UserMapper;
import com.dd.ai_smart_course.service.base.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UserImpl implements UserService {

    //搜索相关
    //默认的偏移量和数量限制
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 0;

    //User类的role属性常量
    private static final String USER_ROLE_ADMIN = "ROLE_ADMIN";
    private static final String USER_ROLE_TEACHER = "ROLE_TEACHER";
    private static final String USER_ROLE_STUDENT = "ROLE_STUDENT";
    public static final List<String> ALLOWED_USER_ROLE = Arrays.asList(USER_ROLE_ADMIN, USER_ROLE_TEACHER, USER_ROLE_STUDENT);

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
    public List<User> getUsersByStatus(String status, boolean isDESC, String order, Integer limit, Integer offset){
        //是否采用默认的数量限制
        if(limit == null){
            limit = DEFAULT_LIMIT;
        }
        //是否采用默认的偏移量
        if(offset == null){
            offset = DEFAULT_OFFSET;
        }

        //检测比较参数和排序参数是否合法
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
    public List<User> getUsersByUsername(String username, boolean isDESC, String order, Integer limit, Integer offset){
        //是否采用默认的数量限制
        if(limit == null){
            limit = DEFAULT_LIMIT;
        }
        //是否采用默认的偏移量
        if(offset == null){
            offset = DEFAULT_OFFSET;
        }

        //检测比较参数和排序参数是否合法
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
    public List<User> getUsersByRole(String role, boolean isDESC, String order, Integer limit, Integer offset){
        //是否采用默认的数量限制
        if(limit == null){
            limit = DEFAULT_LIMIT;
        }
        //是否采用默认的偏移量
        if(offset == null){
            offset = DEFAULT_OFFSET;
        }

        //检测比较参数和排序参数是否合法
        if(!ALLOWED_USER_ROLE.contains(role) || !ALLOWED_USER_COLUMN_NAME.contains(order)){
            return null;
        }

        if(isDESC){
            return userMapper.getUsersByAllParam(SQL_USER_ROLE, role, order, SQL_DESCENDING, limit, offset);
        }else {
            return userMapper.getUsersByAllParam(SQL_USER_ROLE, role, order, SQL_ASCENDING, limit, offset);
        }
    }

    //添加用户
    @Override
    public int addUser(User user) throws BusinessException {
        try {
            checkFactor(user);
            return userMapper.addUser(user);
        } catch (BusinessException be){
            log.error("can't add in \"users\" table: " + be.getMessage());
            throw be;
        } catch (Exception e){
            log.error("can't add in \"users\" table: " + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    //修改用户
    @Override
    public int updateUser(User user) throws BusinessException {
        try{
            checkUserExists(user.getId());
            checkFactor(user);
            return userMapper.updateUser(user);
        } catch (BusinessException be){
            log.error("can't update in \"users\" table.BE code = " + be.getCode());
            throw be;
        } catch (Exception e){
            log.error("can't update in \"users\" table because of unknown error." + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    //删除用户
    @Override
    public int deleteUser(int id) {
        int result = userMapper.deleteUser(id);
        if(result == 0){
            log.warn("can't delete in \"users\" table");
        }
        return result;
    }

    //更新用户角色
    @Override
    public boolean updateUserRole(int userId, String role) throws BusinessException{
        checkUserExists(userId);
        if(!ALLOWED_USER_ROLE.contains(role)){
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }
        userMapper.updateUserRole(userId, role);
        return true;
    }

    //更新用户状态
    @Override
    public boolean updateUserStatus(int userId, String status) throws BusinessException{
        checkUserExists(userId);
        if(!ALLOWED_USER_STATUS.contains(status)){
            throw new BusinessException(ErrorCode.STATUS_ERROR);
        }
        userMapper.updateUserStatus(userId, status);
        return true;
    }

    //更新用户密码
    @Override
    public boolean updateUserPassword(int userId, String password) throws BusinessException{
        checkUserExists(userId);
        userMapper.updateUserPassword(userId, password);
        return true;
    }

    //更新用户名称
    @Override
    public boolean updateUsername(int userId, String username) throws BusinessException{
        checkUserExists(userId);
        userMapper.updateUsername(userId, username);
        return true;
    }


    //检验关键数据是否非法
    //若姓名、邮箱和电话非法，则抛出对应异常
    //针对add，update
    @Override
    public void checkFactor(User user) throws BusinessException{

        String role = user.getRole();
        String name = user.getName();
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();

        //检查用户角色
        if(role == null){
            throw new BusinessException(ErrorCode.ROLE_NULL);
        }
        if(ALLOWED_USER_ROLE.contains(role)){
            throw new BusinessException(ErrorCode.ROLE_ERROR);
        }

        //检查用户名
        if(name == null){
            throw new BusinessException(ErrorCode.USERNAME_NULL);
        }
        if(!isNameUnique(name)){
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }

        //检查电话号码
        if(phoneNumber == null){
            throw new BusinessException(ErrorCode.PHONE_NULL);
        }
        if(!isPhoneNumberUnique(phoneNumber)){
            throw new BusinessException(ErrorCode.PHONE_EXISTS);
        }

        //检查邮箱号码
        if(email == null){
            throw new BusinessException(ErrorCode.EMAIL_NULL);
        }
        if(!isEmailUnique(email)){
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }
    }

    //根据userId检查用户是否存在
    //针对update
    @Override
    public void checkUserExists(int userId) throws BusinessException{
        if(userId == 0){
            throw new BusinessException(ErrorCode.USER_ID_NULL);
        }
        if(userMapper.getUserById(userId) == null){
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
    }

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
package com.dd.ai_smart_course.service.impl;

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

    @Override
    public int addUser(User user) throws BusinessException {
        try {
            return userMapper.addUser(user);
        } catch (BusinessException be){
            log.error("can't add in \"users\" table.BE code = " + be.getCode());
            throw be;
        } catch (Exception e){
            log.error("can't add in \"users\" table because of unknown error." + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int updateUser(User user) throws BusinessException {
        try{
            chechFactor(user);
            return userMapper.updateUser(user);
        } catch (BusinessException be){
            log.error("can't update in \"users\" table.BE code = " + be.getCode());
            throw be;
        } catch (Exception e){
            log.error("can't update in \"users\" table because of unknown error." + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int deleteUser(int id) {
        int result = userMapper.deleteUser(id);
        if(result == 0){
            log.warn("can't delete in \"users\" table");
        }
        return result;
    }


    //检验关键数据是否为空
    //若姓名、邮箱和电话为空，则抛出对应异常
    public void chechFactor(User user) throws BusinessException{

        String name = user.getName();
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();

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
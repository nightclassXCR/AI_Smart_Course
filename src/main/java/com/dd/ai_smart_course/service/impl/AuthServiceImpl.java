package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import com.dd.ai_smart_course.service.base.AuthService;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.exception.errorcode.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    //接入数据库中user图表
    @Autowired
    private UserMapper userMapper;

    //JWT令牌相关
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    //使用邮箱地址登录
    @Override
    public LocalToken loginByEmail(String email, String password) throws BusinessException{
        LocalToken response = new LocalToken();


        User user = getUserByEmail(email);
        if(user == null){
            //用户不存在，提示注册
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }else {
            String db_password = user.getPassword();
            if(password.equals(db_password)){
                //用户名和密码匹配
                return encapsulateToken(user);
            }else{
                throw new BusinessException(ErrorCode.PASSWORD_WRONG);
            }
        }
    }

    //使用电话号码登录
    @Override
    public LocalToken loginByPhoneNumber(String phoneNumber, String password) throws BusinessException{
        LocalToken response = new LocalToken();

        User user = getUserByPhone(phoneNumber);
        if(user == null){
            //用户不存在，提示注册
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }else {
            String db_password = user.getPassword();
            if(password.equals(db_password)){
                //用户名和密码匹配
                return encapsulateToken(user);
            }else{
                throw new BusinessException(ErrorCode.PASSWORD_WRONG);
            }
        }

    }

    //使用电话号码得到User
    public User getUserByPhone(String phoneNumber){
        Integer dbID = userMapper.getIDByPhoneNumber(phoneNumber);
        if(dbID == null){
            return null;
        }
        User user = userMapper.getUserById(dbID);
        return user;
    }

    //使用邮箱号码得到User
    public User getUserByEmail(String email){
        Integer dbID = userMapper.getIDByEmail(email);
        if(dbID == null){
            return null;
        }
        User user = userMapper.getUserById(dbID);

        return user;
    }

    //创建token
    public String generateToken(User user){
        String token = jwtTokenUtil.generateToken("USER_LOCAL_TOKEN", String.valueOf(user.getId()), user.getUsername(), user.getRole(), user.getStatus());
        return token;
    }

    //创建LocalToken
    public LocalToken encapsulateToken(User user){
        String token = generateToken(user);

        //清除User实体对象的敏感数据
        user.setPassword(null);

        LocalToken response = new LocalToken();
        response.setToken(token);
        response.setUser(user);

        return response;
    }
}

package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceImpl {

    //接入数据库中user图表
    @Autowired
    private UserMapper userMapper;

    //JWT令牌相关
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    //使用邮箱地址登录
    public LocalToken loginByEmail(String email, String password){
        LocalToken response = new LocalToken();
        String token = "";
        String status = "";

        User user = getUserByEmail(email);
        if(user == null){
            //用户不存在，提示注册
            token = generateToken(user);
            status = "wrong credential";
        }else {
            String db_password = user.getPassword();
            if(password.equals(db_password)){
                //用户名和密码匹配
                status = "ok";
            }else{
                status =  "wrong password";
            }
        }

        //清空敏感数据
        user.setPassword("");

        response.setToken(token);
        response.setUser(user);

        return response;
    }

    //使用电话号码登录
    public LocalToken loginByPhoneNumber(String phoneNumber, String password){
        LocalToken response = new LocalToken();
        String token = "";
        String status = "";

        User user = getUserByPhone(phoneNumber);
        if(user == null){
            //用户不存在，提示注册
            status = "wrong credential";
        }else {
            String db_password = user.getPassword();
            if(password.equals(db_password)){
                //用户名和密码匹配
                token = generateToken(user);
                status = "ok";
            }else{
                status = "wrong password";
            }
        }


        response.setToken(token);
        response.setUser(user);

        return response;
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
        String token = jwtTokenUtil.generateToken("LOGIN", String.valueOf(user.getId()), user.getUsername(), user.getRole(), user.getStatus());
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

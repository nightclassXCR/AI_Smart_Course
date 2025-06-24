package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import com.dd.ai_smart_course.service.base.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenImpl implements TokenService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    private static final String USER_STATUS_NORMAL = "STATUS_NORMAL";   //初步定义用户的正常状态
    private static final String USER_ROLE_ADMIN = "ROLE_ADMIN"; //初步定义用户的管理员身份

    @Override
    public boolean checkToken(LocalToken localToken){
        User user = getDBUser(localToken);

        if(user == null){
            return false;
        }

        //根据数据库中的user对象的信息判断
        if(user.getStatus() != USER_STATUS_NORMAL){
            return false;
        }

        return true;
    }

    //根据token获取数据库对应User对象
    private User getDBUser(LocalToken localToken){
        String token = localToken.getToken();

        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        if(userId == null || jwtTokenUtil.isTokenExpired(token)){
            return null;
        }

        //根据token中的userID获取数据库中的user对象
        return userMapper.getUserById(userId);
    }
}

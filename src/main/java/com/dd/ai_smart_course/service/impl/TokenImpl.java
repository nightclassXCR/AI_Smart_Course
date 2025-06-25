package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import com.dd.ai_smart_course.service.base.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenImpl implements TokenService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    private static final String USER_STATUS_NORMAL = "STATUS_NORMAL";   //初步定义用户的正常状态
    private static final String USER_ROLE_ADMIN = "ROLE_ADMIN"; //初步定义用户的管理员身份

    // 检测token的有效性
    @Override
    public boolean checkToken(LocalToken localToken){
        User tokenUser = getDBUser(localToken);
        String token = localToken.getToken();

        boolean isValid = false;

        try {
            isValid = jwtTokenUtil.validateToken(token, String.valueOf(tokenUser.getId()));
            //输出日志
            jwtTokenUtil.printToken(token);
        }catch (Exception e){
            log.error("resolve fails: " + e.getMessage());
        }
        log.info("LocalToken resolution result: " + isValid);

        return isValid;
    }

    //从令牌中验证管理员身份有效性
    @Override
    public boolean checkAdmin(LocalToken localToken){
        //判断令牌有效性
        boolean isTokenValid = checkToken(localToken);
        User tokenUser = getDBUser(localToken);
        boolean isAdmin = false;

        if(isTokenValid){
            String token = localToken.getToken();
            isAdmin =  tokenUser.getRole().equals(USER_ROLE_ADMIN);
        }else {
            isAdmin = false;
        }

        //生成日志
        log.info("LocalToken ADMIN role resolution result: " + isAdmin);

        return isAdmin;
    }

    //从令牌中验证用户状态正常
    @Override
    public boolean checkNormal(LocalToken localToken){
        //判断令牌有效性
        boolean isTokenValid = checkToken(localToken);
        User tokenUser = getDBUser(localToken);
        boolean isNormal = false;

        if(isTokenValid){
            String token = localToken.getToken();
            isNormal =  tokenUser.getStatus().equals(USER_STATUS_NORMAL);
        }else {
            isNormal  = false;
        }

        //生成日志
        log.info("LocalToken NORMAL status resolution result: " + isNormal);

        return isNormal;
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

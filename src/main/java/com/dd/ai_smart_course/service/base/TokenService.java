package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;

public interface TokenService {
    //检验token有效性
    boolean checkToken(LocalToken localToken);

    //检验admin身份
    boolean checkAdmin(LocalToken localToken);

    //检验teacher身份
    boolean checkTeacher(LocalToken localToken);

    //检验normal状态
    boolean checkNormal(LocalToken localToken);
}

package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.LocalToken;

public interface TokenService {
    //检验token有效性
    boolean checkToken(LocalToken localToken);
}

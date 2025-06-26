package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.exception.BusinessException;

public interface AuthService {
    LocalToken loginByEmail(String email, String password) throws BusinessException;

    LocalToken loginByPhoneNumber(String phoneNumber, String password) throws BusinessException;

    boolean register(User user) throws BusinessException;
}

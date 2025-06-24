package com.dd.ai_smart_course.service.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String phoneNumber;
    private String password;
}

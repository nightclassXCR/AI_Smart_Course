package com.dd.ai_smart_course.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String phoneNumber;
    private String password;
}

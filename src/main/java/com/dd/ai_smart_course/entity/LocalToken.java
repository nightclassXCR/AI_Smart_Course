package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class LocalToken {
    private User user;
    private String token;
}

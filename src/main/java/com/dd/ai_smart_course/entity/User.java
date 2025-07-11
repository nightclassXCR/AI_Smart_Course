package com.dd.ai_smart_course.entity;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String name;
    private String role;    //"ROLE_ADMIN" OR "ROLE_STUDENT" OR "ROLE_TEACHER"
    private Timestamp createdAt;
    private Timestamp lastActivityAt;
    private String status;  //"STATUS_NORMAL" OR "STATUS_BANNED"

}

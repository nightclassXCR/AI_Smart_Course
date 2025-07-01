package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {
    private String subject; //需要修改的变量名，“modify_username”, "modify_password",  "modify_role", "modify_status"
    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String name;
    private String role;    //"ROLE_ADMIN" OR "ROLE_STUDENT" OR "ROLE_TEACHER"
    private String status;  //"STATUS_NORMAL" OR "STATUS_BANNED"
}

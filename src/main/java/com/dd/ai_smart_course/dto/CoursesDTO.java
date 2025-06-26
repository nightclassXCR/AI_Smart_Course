package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoursesDTO {
    private int id;
    private String name;
    private String description;
    private int teacherId; // 可以保留教师ID
    private String teacherName; // <-- 这是你想要添加的教师名字字段
    // private String status;

}

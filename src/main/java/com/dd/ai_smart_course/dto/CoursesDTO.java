package com.dd.ai_smart_course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursesDTO {
    private int id;
    private String name;
    private int teacherId; // 可以保留教师ID
    private String description;
    private int credit;
    private int hours;
    private String teacherRealName; // <-- 这是你想要添加的教师名字字段
    private String statusSelf;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String statusStudent;
    private int studentCount; // <-- 新增字段，用于存储学生数量
    private int averageScore; // <-- 新增字段，用于存储平均分


}

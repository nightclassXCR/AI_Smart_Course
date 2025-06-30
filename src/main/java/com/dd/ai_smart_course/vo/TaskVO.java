package com.dd.ai_smart_course.vo;

import com.dd.ai_smart_course.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    private Integer id;
    private String title;
    private String courseName;
    private Task.Type type;
    private Task.Status status;
    private LocalDateTime deadline;


}

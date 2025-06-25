package com.dd.ai_smart_course.dto;

import com.dd.ai_smart_course.entity.Question;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {

    private String title;
    private int courseId;
    private LocalDateTime deadline;
    private List<Question> questions;
}

package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class UserTask {

    private int userId;
    private int taskId;
    private Status status;

    public enum Status {
        uncompleted, completed
    }
}

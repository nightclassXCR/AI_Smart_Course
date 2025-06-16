package com.dd.ai_smart_course.Entity;


import lombok.Data;

import java.sql.Timestamp;


@Data
public class Task {
    private int id;
    private int courseId;
    private String title;
    private String type;// homework project reading
    private Timestamp deadline;
    private Timestamp createdAt;

}

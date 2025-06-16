package com.dd.ai_smart_course.Entity;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Recommend {
    private int id;
    private int userId;
    private String recommendType; // concept, path
    private int recommendId;
    private String reason;
    private Timestamp createdAt;
}

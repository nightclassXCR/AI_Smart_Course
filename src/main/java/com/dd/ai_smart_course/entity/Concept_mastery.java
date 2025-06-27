package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Concept_mastery {
    private int userId;
    private int conceptId;
    private double masteryLevel;
    private LocalDateTime lastPracticed;

    public Concept_mastery(int userId, int conceptId, int newMasteryLevel, LocalDateTime now) {
    }
}

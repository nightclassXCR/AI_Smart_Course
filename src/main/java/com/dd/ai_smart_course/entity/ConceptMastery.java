package com.dd.ai_smart_course.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptMastery {
    private int userId;
    private int conceptId;
    private int masteryLevel;
    private LocalDateTime lastUpdated;

    public ConceptMastery(Integer userId, Integer conceptId, int currentMasteryLevel) {
        this.userId = userId;
        this.conceptId = conceptId;
        this.masteryLevel = currentMasteryLevel;
    }
}

package com.dd.ai_smart_course.entity;

import java.time.LocalDateTime;

public class Concept_mastery {
    private Long user_id;
    private Long concept_id;
    private double mastery_level;
    private LocalDateTime last_practiced;

    public Concept_mastery(Long userId, Long conceptId, int newMasteryLevel, LocalDateTime now) {
        this.user_id = userId;
        this.concept_id = conceptId;
        this.mastery_level = newMasteryLevel;
        this.last_practiced = now;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getConcept_id() {
        return concept_id;
    }

    public void setConcept_id(Long concept_id) {
        this.concept_id = concept_id;
    }

    public double getMastery_level() {
        return mastery_level;
    }

    public void setMastery_level(double mastery_level) {
        this.mastery_level = mastery_level;
    }

    public LocalDateTime getLast_practiced() {
        return last_practiced;
    }

    public void setLast_practiced(LocalDateTime last_practiced) {
        this.last_practiced = last_practiced;
    }
}

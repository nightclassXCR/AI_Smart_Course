package com.dd.ai_smart_course.entity;

import java.time.LocalDateTime;

public class Concept_mastery {
    private int id;
    private int concept_id;
    private double mastery_level;
    private LocalDateTime last_practiced;

    public int getConcept_id() {
        return concept_id;
    }

    public void setConcept_id(int concept_id) {
        this.concept_id = concept_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

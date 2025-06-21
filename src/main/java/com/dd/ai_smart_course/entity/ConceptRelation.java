package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class ConceptRelation {
    private int id;
    private int sourceConceptId;
    private int targetConceptId;
    private String relationType; // 如 prerequisite、related、example 等

    // getter/setter
}
package com.dd.ai_smart_course.entity;

public class ConceptRelation {
    private int id;
    private int sourceConceptId;
    private int targetConceptId;
    private String relationType; // 如 prerequisite、related、example 等

    // 构造函数
    public ConceptRelation() {}
    
    public ConceptRelation(int sourceConceptId, int targetConceptId, String relationType) {
        this.sourceConceptId = sourceConceptId;
        this.targetConceptId = targetConceptId;
        this.relationType = relationType;
    }

    // getter/setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceConceptId() {
        return sourceConceptId;
    }

    public void setSourceConceptId(int sourceConceptId) {
        this.sourceConceptId = sourceConceptId;
    }

    public int getTargetConceptId() {
        return targetConceptId;
    }

    public void setTargetConceptId(int targetConceptId) {
        this.targetConceptId = targetConceptId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
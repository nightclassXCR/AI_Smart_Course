package com.dd.ai_smart_course.entity;

public class Concept_question {
    private int concept_id;
    private int question_id;
    private double weight;

    public int getConcept_id() {
        return concept_id;
    }

    public void setConcept_id(int concept_id) {
        this.concept_id = concept_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

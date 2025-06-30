package com.dd.ai_smart_course.entity;


import lombok.Data;

import java.util.Objects;

@Data
public class Concept {
    private int id;
    private int chapterId;
    private String name;
    private String description;
    private String importance;// low, medium, high
    private int resourceId;
    private String createdAt;
    private String updatedAt;
    @Override
    public String toString() {
        return "Concept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", chapterId=" + chapterId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concept concept = (Concept) o;
        return Objects.equals(id, concept.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

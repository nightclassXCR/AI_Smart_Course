package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class Course {
    private int id;
    private String name;
    private int teacherId;
    private String description;
    private String status;// published,draft,archived
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.dd.ai_smart_course.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Builder
@Data
public class Course {
    private int id;
    private String name;
    private int teacherId;
    private String description;
    private String credit;
    private int hours;
    private String statusSelf;// published,draft,archived
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String statusStudent;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", description='" + description + '\'' +
                ", status='" + statusSelf + '\'' +
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

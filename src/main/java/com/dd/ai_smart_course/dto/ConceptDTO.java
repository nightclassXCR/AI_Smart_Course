package com.dd.ai_smart_course.dto;

import com.dd.ai_smart_course.entity.Concept;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptDTO {
    private int id;
    private Integer chapterId;
    private String name;
    private String description;
    private String importance;// low, medium, high
    private int resourceId;
    private String createdAt;
    private String updatedAt;
    private String chapterName;

}

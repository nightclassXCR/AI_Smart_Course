package com.dd.ai_smart_course.service.dto;

import com.dd.ai_smart_course.entity.Concept;
import lombok.Data;

@Data
public class ConceptDTO extends Concept {
    private String chapterName;

    public ConceptDTO(String chapterName){
        this.chapterName = chapterName;
    }

    public ConceptDTO(Concept concept){
        this.setId(concept.getId());
        this.setChapterId(concept.getChapterId());
        this.setName(concept.getName());
        this.setDescription(concept.getDescription());

    }

    public ConceptDTO(Concept concept, String chapterName){
        this.chapterName = chapterName;
        this.setId(concept.getId());
        this.setChapterId(concept.getChapterId());
        this.setName(concept.getName());
        this.setDescription(concept.getDescription());

    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}

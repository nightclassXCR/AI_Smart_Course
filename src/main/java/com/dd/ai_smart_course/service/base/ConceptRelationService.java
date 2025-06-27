package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.ConceptRelation;

import java.util.List;

public interface ConceptRelationService {

    int addRelation(ConceptRelation relation);
    int deleteRelation(int id);
    List<ConceptRelation> getRelationsByConcept(int conceptId);
    
}

package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.ConceptRelation;
import com.dd.ai_smart_course.mapper.ConceptRelationMapper;
import com.dd.ai_smart_course.service.base.ConceptRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConceptRelationImpl implements ConceptRelationService {
    @Autowired
    private ConceptRelationMapper mapper;

    @Override
    public int addRelation(ConceptRelation relation) {
        return mapper.addRelation(relation);
    }

    @Override
    public int deleteRelation(int id) {
        return mapper.deleteRelation(id);
    }

    @Override
    public List<ConceptRelation> getRelationsByConcept(int conceptId) {
        return mapper.getRelationsByConcept(conceptId);
    }
}
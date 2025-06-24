package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.ConceptRelation;
import com.dd.ai_smart_course.service.ConceptRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concept-relations")
public class ConceptRelationController {

    @Autowired
    private ConceptRelationService conceptRelationService;

    @PostMapping
    public int addRelation(@RequestBody ConceptRelation relation) {
        return conceptRelationService.addRelation(relation);
    }

    @DeleteMapping("/{id}")
    public int deleteRelation(@PathVariable int id) {
        return conceptRelationService.deleteRelation(id);
    }

    @GetMapping("/concept/{conceptId}")
    public List<ConceptRelation> getRelationsByConcept(@PathVariable int conceptId) {
        return conceptRelationService.getRelationsByConcept(conceptId);
    }
}
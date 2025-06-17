package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.service.ConceptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concepts")
@Slf4j
public class ConceptController {

    @Autowired
    private ConceptService conceptService;

    /**
     * 获取所有概念
     * @return
     */
    @GetMapping
    public Result<List<Concept>> getAllConcepts() {

        return Result.success(conceptService.getAllConcepts());
    }
    /**
     * 获取某个章节的 concept
     * @param chapterId
     * @return
     */
    @GetMapping("/{chapterId}")
    public Result<List<Concept>> getConceptsByChapterId(@PathVariable int chapterId) {

        return Result.success(conceptService.getConceptsByChapterId(chapterId));
    }
    /**
     * 添加 concept
     * @param conceptDto
     * @return
     */
    @PostMapping
    public Result<String> addConcept(@RequestBody ConceptDTO conceptDto) {
        conceptService.addConcept(conceptDto);
        return Result.success("添加成功");
    }
    /**
     * 更新 concept
     * @param conceptDto
     * @return
     */
    @PutMapping
    public Result<String> updateConcept(@RequestBody ConceptDTO conceptDto) {
        conceptService.updateConcept(conceptDto);
        return Result.success("更新成功");
    }
    /**
     * 删除 concept
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteConcept(@PathVariable int id) {
        if(conceptService.deleteConcept(id) > 0){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }

}

package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    /**
     * 获取所有章节
     */
    @GetMapping
    public Result<List<Chapter>> getAllChapter() {
            List<Chapter> chapterList = chapterService.getAllChapter();
            return Result.success("获取成功", chapterList);
    }

    /**
     * 根据对应课程ID获取章节
     * @param courseId
     **/
    @GetMapping("/{courseId}")
    public Result<List<Chapter>> getChapterByCourseId(@PathVariable int courseId) {
            List<Chapter> chapterList = chapterService.getChaptersByCourseId(courseId);
            return Result.success("获取成功", chapterList);
    }

    /**
     * 添加章节
     * @param chapterdto
     */
    @PostMapping
    public Result<String> addChapter(@RequestBody ChapterDTO chapterdto) {
            int result = chapterService.addChapter(chapterdto);
            if (result > 0) {
                return Result.success("添加成功");
            } else {
                return Result.error("添加失败");
            }
    }
    /**
     * 更新章节
     * @param chapterdto
     */
    @PutMapping
    public Result<String> updateChapter(@RequestBody ChapterDTO chapterdto) {
            int result = chapterService.updateChapter(chapterdto);
            if (result > 0) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
    }

    /**
     * 删除章节
     * @param id
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteChapter(@PathVariable int id) {
            int result = chapterService.deleteChapter(id);
            if (result > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
    }



}

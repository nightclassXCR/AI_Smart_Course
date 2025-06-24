package com.dd.ai_smart_course.service.base;


import com.dd.ai_smart_course.service.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;

import java.util.List;

public interface ChapterService {
    // 获取所有章节
    List<Chapter> getAllChapter();

    // 获取指定课程的章节
    List<Chapter> getChaptersByCourseId(int courseId);

    // 添加章节
    int addChapter(ChapterDTO chapterdto);

    // 更新章节
    int updateChapter(ChapterDTO chapterdto);

    // 删除章节
    int deleteChapter(int id);

    // 重新排序章节
    void reorderChapters(Long courseId, List<Long> orderedChapterIds);

    // 获取某课程下的所有知识点
    List<Concept> getConceptsByChapterId(Long chapterId);



}

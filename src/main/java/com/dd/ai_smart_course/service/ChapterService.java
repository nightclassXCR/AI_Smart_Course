package com.dd.ai_smart_course.service;


import com.dd.ai_smart_course.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;

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

}

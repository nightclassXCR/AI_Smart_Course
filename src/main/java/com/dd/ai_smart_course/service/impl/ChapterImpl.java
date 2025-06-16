package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dd.ai_smart_course.mapper.ChapterMapper;

import java.util.List;

@Service
public class ChapterImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    // 查询所有章节
    @Override
    public List<Chapter> getAllChapter() {
        return chapterMapper.getAllChapters();
    }

    // 根据课程id查询章节
    @Override
    public List<Chapter> getChaptersByCourseId(int courseId) {
        return chapterMapper.getChaptersByCourseId(courseId);
    }

    // 添加章节
    @Override
    public int addChapter(ChapterDTO chapterdto) {
        Integer courseId = chapterMapper.getCourseIdByCourseName(chapterdto.getCourseName());
        if (courseId == null) {
            throw new RuntimeException("课程不存在");
        }
        chapterdto.setCourseId(courseId);
        return chapterMapper.addChapter(chapterdto);
    }

    // 更新章节
    @Override
    public int updateChapter(ChapterDTO chapterdto) {
        Integer courseId = chapterMapper.getCourseIdByCourseName(chapterdto.getCourseName());
        if (courseId == null) {
            throw new RuntimeException("课程不存在");
        }
        chapterdto.setCourseId(courseId);
        Chapter chapter = new Chapter();
        chapter.setId(chapterdto.getId());
        chapter.setCourseId(courseId);
        chapter.setTitle(chapterdto.getTitle());
        chapter.setContent(chapterdto.getContent());
        chapter.setSortOrder(chapterdto.getSortOrder());
        return  chapterMapper.updateChapter(chapter);
    }

    // 删除章节
    @Override
    public int deleteChapter(int id) {
        return chapterMapper.deleteChapter(id);
    }
}

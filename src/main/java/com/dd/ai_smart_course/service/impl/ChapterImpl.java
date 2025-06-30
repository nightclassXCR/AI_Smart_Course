package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.service.base.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dd.ai_smart_course.mapper.ChapterMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;



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
    @Transactional
    public int addChapter(ChapterDTO chapterdto) {
        Integer courseId = chapterdto.getCourseId();
        if (courseId == null) {
            throw new RuntimeException("课程不存在");
        }
        chapterdto.setCourseId(courseId);
        return chapterMapper.addChapter(chapterdto);
    }

    // 更新章节
    @Override
    @Transactional
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
        chapter.setSequence(chapterdto.getSequence());
        return  chapterMapper.updateChapter(chapter);
    }

    // 删除章节
    @Override
    @Transactional
    public int deleteChapter(int id) {
        // 重要的业务逻辑：删除章节时，需要考虑关联数据的处理
        // 1. 删除所有关联的知识点
        // 2. 删除学习日志、任务等表中与此章节相关的记录 (通过知识点关联或直接关联)
        // 3. 如果是物理删除，确保数据库设置了正确的级联删除或手动处理
        //    或者更常见的做法是进行逻辑删除

        // 示例：此处仅删除章节本身
        // conceptMapper.deleteConceptsByChapterId(id); // 假设 ConceptMapper 有此方法
        return chapterMapper.deleteChapter(id);
    }

    // 重新排序章节
    @Override
    @Transactional
    public void reorderChapters(int courseId, List<Integer> orderedChapterIds) {
        if (orderedChapterIds == null || orderedChapterIds.isEmpty()) {
            return;
        }

        // 1. 校验传入的章节ID是否都属于该课程，并获取现有章节信息
        List<Chapter> existingChapters = chapterMapper.getChaptersByCourseId(courseId); // courseId是Long，但mapper参数是int，注意转换!转换牛魔酬宾，sblong
        Map<Integer, Chapter> existingChapterMap = existingChapters.stream()
                .collect(Collectors.toMap(Chapter::getId, java.util.function.Function.identity()));

        // 2. 遍历新的排序列表，更新每个章节的sequence
        for (int i = 0; i < orderedChapterIds.size(); i++) {
            int chapterId = Math.toIntExact(orderedChapterIds.get(i));
            // 校验章节是否存在且属于当前课程
            if (!existingChapterMap.containsKey(chapterId) || !Objects.equals(existingChapterMap.get(chapterId).getCourseId(), courseId)) {
                throw new IllegalArgumentException("Chapter ID " + chapterId + " is invalid or does not belong to course " + courseId);
            }
            int newSequence = i + 1; // 序列从1开始

            // 只有当sequence发生变化时才更新
            if (!Objects.equals(existingChapterMap.get(chapterId).getSequence(), newSequence)) {
                chapterMapper.updateChapterSequence(chapterId, newSequence);
            }
        }

    }

    // 根据章节id查询 concepts
    @Override
    public List<Concept> getConceptsByChapterId(int chapterId) {
        return chapterMapper.getConceptsByChapterId(chapterId);
    }

    @Override
    public Chapter getChapterContentById(int id) {
        return chapterMapper.getChapterContentById(id);
    }


}

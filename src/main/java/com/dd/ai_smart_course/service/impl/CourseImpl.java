package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.mapper.CourseMapper;
import com.dd.ai_smart_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourse() {
        return courseMapper.getAllCourses();
    }

    @Override
    public Course getCourseById(int id) {
        return courseMapper.getCourseById(id);
    }

    @Override
    @Transactional
    public int addCourse(Course course) {
        return courseMapper.addCourse(course);
    }

    @Override
    @Transactional
    public int updateCourse(Course course) {
        return courseMapper.updateCourse(course);
    }

    @Override
    @Transactional
    public int deleteCourse(int id) {
        // 重要的业务逻辑：删除课程时，需要考虑关联数据的处理
        // 1. 删除所有关联的章节
        // 2. 删除所有关联的知识点
        // 3. 删除 course_user 表中的关联记录
        // 4. 删除 learning_logs, scores, recommendations, qa_records 等表中与此课程相关的记录
        // 5. 如果是物理删除，确保数据库设置了正确的级联删除或手动处理
        //    或者更常见的做法是进行逻辑删除 (设置课程状态为 'deleted'/'archived')

        // 示例：此处仅删除课程本身，实际应根据业务需求完善
        // 假设你有一个 ChapterMapper 和 ConceptMapper 来删除子项
        return courseMapper.deleteCourse(id);
    }

    /**
     * 获取课程的章节
     * @param teacherId
     * @return
     */
    @Override
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseMapper.getCoursesByTeacherId(teacherId);
    }

    /**
     *  获取课程的章节
     * @param courseId
     * @return
     */
    @Override
    public List<Chapter> getChaptersByCourse(Long courseId) {
        return courseMapper.findChaptersForGrouping(courseId);
    }

    // TODO: Implement
    @Override
    public List<Concept> getConceptsByCourse(Long courseId) {
        return courseMapper.getConceptsByCourse(courseId);
    }

    /**
     * 获取课程的章节和知识点
     * @param courseId
     * @return
     */
    @Override
    public Map<Chapter, List<Concept>> getConceptsGroupedByChapter(Long courseId) {
        // 1. 获取课程的所有章节，并按顺序排序
        List<Chapter> chapters = courseMapper.findChaptersForGrouping(courseId);

        // 2. 获取课程下所有知识点 (带章节信息，如果 Mapper 没有直接提供，可能需要二次查询)
        // 这里使用 getConceptsByCourse 来获取所有概念，然后通过概念的 chapterId 找到对应的章节。
        // 缺点是如果某个章节没有知识点，它不会出现在概念列表中。
        // 更好的做法是 Mapper 返回一个包含 Concept 和 Chapter 完整信息的 DTO 列表，
        // 或者像 getConceptsWithChapterInfoByCourse 那样，JOIN 章节信息，然后手动组装
        List<Concept> concepts = courseMapper.getConceptsByCourse(courseId);

        // 使用 LinkedHashMap 保持章节的顺序
        Map<Chapter, List<Concept>> groupedConcepts = new LinkedHashMap<>();

        // 初始化 Map，确保所有章节都作为键存在，即使它们目前没有关联的知识点
        for (Chapter chapter : chapters) {
            groupedConcepts.put(chapter, new ArrayList<>());
        }

        // 将知识点按章节ID分组
        // Note: 使用 Java 8 Stream API 进行分组
        Map<Integer, List<Concept>> conceptsByChapterId = concepts.stream()
                .collect(Collectors.groupingBy(Concept::getChapterId));

        // 将分组后的知识点放入结果 Map 中
        for (Chapter chapter : chapters) {
            List<Concept> chapterConcepts = conceptsByChapterId.getOrDefault(chapter.getId(), new ArrayList<>());
            groupedConcepts.put(chapter, chapterConcepts);
        }

        return groupedConcepts;
    }
}

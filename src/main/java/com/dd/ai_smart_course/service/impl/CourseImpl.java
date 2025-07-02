package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.*;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.*;
import com.dd.ai_smart_course.service.base.CourseService;
import com.dd.ai_smart_course.service.base.UserService;
import com.dd.ai_smart_course.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CourseImpl implements CourseService {


    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private QAMapper qaMapper;

    @Autowired
    private CourseUserMapper courseUserMapper;

//    @Autowired
//    private RecommendMapper recommendMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private TaskMapper taskMapper;


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
        // 5. 物理删除，确保数据库设置了正确的级联删除或手动处理
        if (id <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }

        log.info("Starting hard delete for course ID: {}", id);

        // Check if course exists
        Course existingCourse = courseMapper.getCourseById(id);
        if (existingCourse == null) {
            log.warn("Course not found with ID: {}", id);
            return 0; // Course doesn't exist
        }

        try {
            // CRITICAL: Delete in correct order (child records first to avoid foreign key violations)

            // Step 1: Delete QA records (问答记录) using dedicated mapper
            int qaRecordsDeleted = qaMapper.deleteByCourseId(id);
            log.debug("Deleted {} QA records for course {}", qaRecordsDeleted, id);

//            // Step 2: Delete recommendations (推荐记录) using dedicated mapper
//            int recommendationsDeleted = recommendationMapper.deleteByCourseId(id);
//            log.debug("Deleted {} recommendations for course {}", recommendationsDeleted, id);

//            // Step 3: Delete scores (成绩记录) using dedicated mapper
//            int scoresDeleted = scoreMapper.deleteByCourseId(id);
//            log.debug("Deleted {} scores for course {}", scoresDeleted, id);

//            // Step 4: Delete learning logs (学习日志) using dedicated mapper
//            int learningLogsDeleted = LogMapper.deleteByCourseId(id);
//            log.debug("Deleted {} learning logs for course {}", learningLogsDeleted, id);

            // Step 5: Delete concepts/knowledge points (知识点) using dedicated mapper
            int conceptsDeleted = conceptMapper.deleteByCourseId(id);
            log.debug("Deleted {} concepts for course {}", conceptsDeleted, id);

            // Step 6: Delete chapters (章节) using dedicated mapper
            int chaptersDeleted = chapterMapper.deleteByCourseId(id);
            log.debug("Deleted {} chapters for course {}", chaptersDeleted, id);

//            // Step 7: Delete course-user associations (课程用户关联) using dedicated mapper
//            int courseUserDeleted = courseUserMapper.deleteByCourseId(id);
//            log.debug("Deleted {} course-user associations for course {}", courseUserDeleted, id);

            // Step 8: Finally delete the course itself (删除课程本身)
            int courseDeleted = courseMapper.deleteCourse(id);

            if (courseDeleted > 0) {
                log.info("Successfully hard deleted course {} and all related data", id);
                log.info("Deletion summary - QA: {}, Recommendations: {}, Scores: {}, Logs: {}, Concepts: {}, Chapters: {}, Users: {}",
                        qaRecordsDeleted, conceptsDeleted, chaptersDeleted);
            }

            return courseDeleted;
        } catch (Exception e) {
            log.error("Failed to hard delete course with ID: {}. Error: {}", id, e.getMessage(), e);
            // Transaction will be rolled back automatically due to @Transactional
            throw new RuntimeException("Failed to delete course and related data: " + e.getMessage(), e);
        }
    }

    /**
     * 获取课程的章节
     * @param teacherId
     * @return
     */
    @Override
    public List<CoursesDTO> getCoursesByTeacherId(int teacherId) {

        return courseMapper.getCoursesByTeacherId(teacherId);
    }

    /**
     *  获取课程的章节
     * @param courseId
     * @return
     */
    @Override
    public List<Chapter> getChaptersByCourse(int courseId) {
        return courseMapper.findChaptersForGrouping(courseId);
    }

    /**
     * 获取课程的章节和知识点
     * @param courseId
     * @return
     */
    @Override
    public List<Concept> getConceptsByCourse(int courseId) {
        return courseMapper.getConceptsByCourse(courseId);
    }

    /**
     * 获取课程的章节和知识点
     * @param courseId
     * @return
     */
    @Override
    public Map<Chapter, List<Concept>> getConceptsGroupedByChapter(int courseId) {
        // 1. 获取课程的所有章节，并按顺序排序
        List<Chapter> chapters = courseMapper.findChaptersForGrouping(courseId);

        // 2. 获取课程下所有知识点 (带章节信息，如果 Mapper 没有直接提供，可能需要二次查询)
        // 这里使用 getConceptsByCourse 来获取所有概念，然后通过概念的 chapterId 找到对应的章节。
        // 缺点是如果某个章节没有知识点，它不会出现在概念列表中
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

    /**
     * 添加用户到课程
     * @param userId
     * @param courseId
     */
    @Override
    @Transactional
    public void enrollUserInCourse(int userId, int courseId) {
        if (courseUserMapper.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            throw new IllegalStateException("User already enrolled in this course.");
        }
        Course_user courseUser = new Course_user(courseId, userId, "ROLE_STUDENT");
        courseUserMapper.insertCourseUser(courseUser);
        System.out.println("User " + userId + " enrolled in course " + courseId);

        // **添加：发布用户注册课程事件**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "course",    // targetType
                courseId,    // targetId
                "click",    // actionType: 注册/选课
                null,        // duration
                "{\"action\":\"ENROLL\", \"description\":\"用户注册了课程: " + courseId + "\"}" // detail 明确行为
        ));
    }

    @Override
    @Transactional
    public void unenrollUserFromCourse(int userId, int courseId) {
        Optional<Course_user> cu = courseUserMapper.findByCourseIdAndUserId(courseId, userId);
        if (cu.isEmpty() || !cu.get().getRole().equals("ROLE_STUDENT")) {
            throw new IllegalArgumentException("User is not a student of this course or not enrolled.");
        }
        courseUserMapper.deleteCourseUser(courseId, userId);
        System.out.println("User " + userId + " unenrolled from course " + courseId);

        // **修正：发布用户取消注册课程事件，使用 'click' 并用 detail 区分**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "course",
                courseId,
                "click",       // actionType: 映射为 click (用户点击“退课”按钮)
                null,
                "{\"action\":\"UNENROLL\", \"description\":\"用户退出了课程: " + courseId + "\"}" // detail 明确行为
        ));

    }

    @Override
    @Transactional
    public void completeCourse(int courseId, int userId) {
        Optional<Course_user> cu = courseUserMapper.findByCourseIdAndUserId(courseId, userId);
        if (cu.isEmpty() || !cu.get().getRole().equals("ROLE_STUDENT")) {
            throw new IllegalArgumentException("User is not a student of this course or not enrolled.");
        }
        boolean actuallyCompleted = checkAndMarkCourseCompletion(courseId, userId);

        if (actuallyCompleted) {
            System.out.println("User " + userId + " marked course " + courseId + " as completed.");
            // **修正：发布用户完成课程事件，使用 'click' 或 'view' 并用 detail 区分**
            eventPublisher.publishEvent(new LearningActionEvent(
                    this,
                    userId,
                    "course",
                    courseId,
                    "click",       // actionType: 映射为 click (如果用户点击“标记完成”按钮)
                    null,
                    "{\"action\":\"COMPLETE\", \"description\":\"用户完成了课程: " + courseId + "\"}" // detail 明确行为
            ));
        } else {
            System.out.println("Course " + courseId + " not yet fully completed by user " + userId);
        }

    }

    /**
     *  检查课程是否完成
     * @param courseId
     * @param userId
     * @return
     */
    private boolean checkAndMarkCourseCompletion(int courseId, int userId) {
        List<Chapter> chapters = chapterMapper.getChaptersByCourseId(Math.toIntExact(courseId));
        for (Chapter chapter : chapters) {
            List<Concept> concepts = conceptMapper.getConceptsByChapterId(chapter.getId());
            for (Concept concept : concepts) {
                List<LearningLog> logs = logMapper.getLogsByConceptId(concept.getId());
                for (LearningLog log : logs) {
                    if (log.getUserId()==(userId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void startViewingChapter(int chapterId, int userId) {
        Optional<Chapter> chapterOptional = chapterMapper.findById(chapterId);
        if (chapterOptional.isEmpty()) {
            throw new IllegalArgumentException("Chapter not found: " + chapterId);
        }
        Chapter chapter = chapterOptional.get();
        int courseId = chapter.getCourseId();

        Optional<Course_user> cu = courseUserMapper.findByCourseIdAndUserId(courseId, userId);
        if (cu.isEmpty() || !cu.get().getRole().equals("STUDENT")) {
            throw new IllegalArgumentException("User is not a student of this course or not enrolled.");
        }

        System.out.println("User " + userId + " started viewing chapter " + chapterId);

        // **修正：发布用户开始查看章节事件，使用 'view'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "chapter",
                chapterId,
                "view",        // actionType: 使用 'view'
                null,
                "{\"action\":\"START_VIEW\", \"description\":\"用户开始查看章节: " + chapterId + "\"}" // detail 明确行为
        ));
    }
    @Transactional
    public void completeChapter(int chapterId, int userId) {
        Optional<Chapter> chapterOptional = chapterMapper.findById(chapterId);
        if (chapterOptional.isEmpty()) {
            throw new IllegalArgumentException("Chapter not found: " + chapterId);
        }
        Chapter chapter = chapterOptional.get();
        int courseId = chapter.getCourseId();

        Optional<Course_user> cu = courseUserMapper.findByCourseIdAndUserId(courseId, userId);
        if (cu.isEmpty() || !cu.get().getRole().equals("STUDENT")) {
            throw new IllegalArgumentException("User is not a student of this course or not enrolled.");
        }

        System.out.println("User " + userId + " completed chapter " + chapterId + " in course " + courseId);

        // **修正：发布用户完成章节事件，使用 'click' 并用 detail 区分**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "chapter",
                chapterId,
                "click",       // actionType: 映射为 click (如果用户点击“标记完成”按钮)
                null,
                "{\"action\":\"COMPLETE_CHAPTER\", \"description\":\"用户完成了章节: " + chapterId + "\"}" // detail 明确行为
        ));
    }

    @Override
    public List<CoursesDTO> getMyCourses(int userId) {
        List<Course_user> courseUsers = courseUserMapper.findCoursesByUserId(userId);
        List<CoursesDTO> coursesdto =  new ArrayList<>();
       List<Integer> courseIds = courseUsers.stream()
                .map(Course_user::getCourseId)
                .collect(Collectors.toList());
       if (courseIds.isEmpty()){
           return Collections.emptyList();
       }
//        // 将 List<Integer> 转换为 List<Long>
//        List<Long> longIds = courseIds.stream()
//                .map(Integer::longValue) // 或者 .map(i -> Long.valueOf(i))
//                .collect(Collectors.toList());

       List<Course> courses = courseMapper.getCoursesByIds(courseIds);
        for (Course course : courses) {
            // 确保 course 对象不为 null
            if (course != null) {
                CoursesDTO dto = new CoursesDTO();
                dto.setId(course.getId());
                dto.setName(course.getName());
                dto.setDescription(course.getDescription());
                dto.setTeacherId(course.getTeacherId());
                dto.setStatusSelf(course.getStatusSelf());
                dto.setStatusStudent(course.getStatusStudent());
                List<Integer> taskIdsByCourseId = taskMapper.getTaskIdsByCourseId(course.getId());
                log.info("任务课程id:{}{}",taskIdsByCourseId,course.getId());
                if(taskIdsByCourseId.isEmpty()){
                    dto.setAverageScore(BigDecimal.ZERO);
                }else{
                    dto.setAverageScore(scoreMapper.getAvgScoreByTaskIdAndUserId(BaseContext.getCurrentId(),taskIdsByCourseId));
                }
                log.info("课程平均分:{}{}",dto.getName(),dto.getAverageScore());
                if (course.getTeacherId() != 0) {
                    String teacherName = courseMapper.getUserNameById(course.getTeacherId());
                    dto.setTeacherRealName(teacherName);
                } else {
                    dto.setTeacherRealName("未知教师"); // 默认值
                }
                coursesdto.add(dto);
            }
        }
        return coursesdto;
    }


    @Override
    public PaginationResult<Course> getCourses(int pageNum, int pageSize) {
        return courseMapper.getCourses(pageNum, pageSize);
    }

    // 搜索课程在用户已有课程中
    @Override
    public List<CoursesDTO> searchCourses(String keyword, int userId) {
        List<CoursesDTO> courses = courseMapper.getMyCourses(userId);

        log.info("Searching for courses with keyword: {}", keyword);
        log.info("User's courses count: {}", courses.size());

        // 提前检查：如果用户的课程列表为空，立即返回空列表。
        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        // 关键词过滤：筛选课程名称或描述中包含关键词的课程
        return courses.stream()
                .filter(course -> course.getName().contains(keyword) || course.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }


    @Override
    public List<CoursesDTO> getCoursesNotMyCourses(int useId) {
        return courseMapper.getNotMyCourses(useId);
    }

    @Override
    public String getUserNameById(int userId) {
        return courseMapper.getUserNameById(userId);
    }

    // 结课
    @Override
    public void comleteCourse(int courseId) {
        courseMapper.completeCourse(courseId);
    }


    // 获得已经完结的课程数量
    @Override
    public int getCompletedCourseCount(int userId){
        List<CoursesDTO> courses = courseMapper.getMyCourses(userId);
        for(CoursesDTO course : courses){
            if(course.getStatusStudent().equals("completed")){
                return courses.size();
            }
        }
        return 0;
    }

    @Override
    public int getCouresCountByTeacherId(int teacherId) {
        List<Integer> courseIds = courseMapper.getCoursesCountByTeacherId(teacherId);
        if (courseIds.isEmpty()) {
            return 0;
        }
        return courseIds.size();

    }

    // 获取某门课下的所有学生ID
    @Override
    public List<Integer> getStudentsIDByCourseId(int courseId) {
        return courseUserMapper.findStudentIdsByCourseId(courseId);
    }

    // 获取某门课下的所有学生
    @Override
    public List<User> getStudentsByCourseId(int courseId) {
        List<Integer> studentIds = courseUserMapper.findStudentIdsByCourseId(courseId);
        if (studentIds.isEmpty()) {
            //Collections.emptyList()返回的是一个单例对象（全局唯一），多次调用返回的是同一个实例
            //在日常开发中，建议优先使用Collections.emptyList()或List.of()返回空列表，而不是手动创建new ArrayList<>()
            return Collections.emptyList();
        }
        List<User> students = userService.getUsersByIds(studentIds);
        for(User student : students){
            // 清除敏感数据
            student.setPassword(null);
        }
        return students;
    }
}

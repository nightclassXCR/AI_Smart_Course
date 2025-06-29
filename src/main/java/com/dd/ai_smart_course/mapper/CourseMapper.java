package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 获取所有课程
    @Select("SELECT * FROM courses")
    List<Course> getAllCourses();

    // 获取课程详情
    @Select("SELECT * FROM courses WHERE id = #{id}")
    Course getCourseById(int id);

    // 添加课程
    @Insert("INSERT INTO courses (name, teacher_id, description,credit,hours,status_self, created_at) VALUES (#{name}, #{teacherId}, #{description},#{credit},#{hours},#{statusSelf}, #{createdAt})")
    @Results({
            @Result(property = "description", column = "description")
    })
    int addCourse(Course course);

    // 更新课程信息
    @Update("UPDATE courses SET name = #{name}, teacher_id = #{teacherId}, description = #{description}, created_at = #{createdAt} WHERE id = #{id}")
    @Results({
            @Result(property = "description", column = "description")
    })
    int updateCourse(Course course);

    // 删除课程
    @Delete("DELETE FROM courses WHERE id = #{id}")
    // 注意：删除课程可能需要处理级联删除（例如：相关章节、知识点、用户课程关联、学习日志、成绩等）
    // 在实际生产中，通常会使用逻辑删除（软删除）或者在Service层进行事务管理和相关联数据的删除操作。
    // 这里仅删除课程本身。
    int deleteCourse(int id);



    /**
     * 获取课程详细信息
     * @return 课程列表
     */
    @Select({
            "SELECT",
            "    c.id,",
            "    c.name,",
            "    c.teacher_id,",
            "    c.description,",
            "    c.credit,",
            "    c.hours,",
            "    c.status_self,",
            "    u.name AS teacherRealName,", // 教师真实姓名
            "    c.created_at,",
            "    c.updated_at,",
            "    c.status_student,",
            "    COUNT(DISTINCT cu.user_id) AS studentCount,", // 学生数量
            "    COALESCE(AVG(s.final_score), 0) AS averageScore", // 平均分
            "FROM",
            "    courses c",
            "LEFT JOIN",
            "    users u ON c.teacher_id = u.id",
            "LEFT JOIN",
            "    course_user cu ON c.id = cu.course_id AND cu.role = 'ROLE_STUDENT'",
            "LEFT JOIN",
            "    tasks t ON c.id = t.course_id",
            "LEFT JOIN",
            "    scores s ON cu.user_id = s.user_id AND t.id = s.task_id",
            "WHERE",
            "    c.teacher_id = #{teacherId}",
            "GROUP BY",
            "    c.id, c.name, c.teacher_id, c.description, c.credit, c.hours, c.status_self,",
            "    u.name, c.created_at, c.updated_at, c.status_student"
    })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "description", column = "description"),
            @Result(property = "credit", column = "credit"),
            @Result(property = "hours", column = "hours"),
            @Result(property = "statusSelf", column = "status_self"),
            @Result(property = "teacherRealName", column = "teacherRealName"), // 映射教师真实姓名
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "statusStudent", column = "status_student"),
            @Result(property = "StudentCount", column = "studentCount"), // 映射学生数量
            @Result(property = "averageScore", column = "averageScore") // 映射平均分
    })
    List<CoursesDTO> getCoursesByTeacherId(@Param("teacherId") int teacherId);

    /**
     * 根据课程ID列表获取课程
     * @param courseIds 课程ID列表
     * @return 课程列表
     */
    // 修改 getCoursesByIds 方法：
    // - 联查 user 表以获取教师名字
    // - 使用 AS 别名将教师名字命名为 `teacher_real_name`
    @Select("<script>" +
            "SELECT " +
            "c.id, c.name, c.description, c.teacher_id, c.status_self, c.status_student, " + // Course 实体中有的字段
            "u.name AS teacher_real_name " + // <-- ！！！ 关键：额外查询教师的名字并起别名 ！！！
            "FROM courses c " +
            "JOIN users u ON c.teacher_id = u.id " + // 联接用户表获取教师信息
            "WHERE c.id IN " +
            "<foreach item='id' collection='courseIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(property = "description", column = "description")
    })
    List<Course> getCoursesByIds(@Param("courseIds") List<Integer> courseIds);

    /**
     * 获取课程下所有知识点 (通过 chapters 表关联)
     */
    @Select("SELECT c.id, c.chapter_id, c.name, c.description,c.importance " +
            "FROM concepts c " +
            "JOIN chapters chap ON c.chapter_id = chap.id " +
            "WHERE chap.course_id = #{courseId}")
    @Results({
            @Result(property = "description", column = "description")
    })
    List<Concept> getConceptsByCourse(@Param("courseId") int courseId);

    /**
     * 【辅助方法】用于 CourseService 中按章节分组知识点时获取所有章节的知识点，
     * 包括那些可能没有知识点的章节 (LEFT JOIN)
     * 这里返回的是原始的 Concept 列表，Service 层进行分组
     */
    @Select("SELECT " +
            "    c.id AS concept_id, c.chapter_id, c.name AS concept_name, c.description AS concept_description, " +
            "    chap.id AS chapter_id_fk, chap.course_id, chap.title AS chapter_title, chap.sequence " +
            "FROM concepts c " +
            "RIGHT JOIN chapters chap ON c.chapter_id = chap.id " + // RIGHT JOIN 确保所有章节都被选中
            "WHERE chap.course_id = #{courseId} " +
            "ORDER BY chap.sequence ASC, c.id ASC") // 确保章节和知识点都有序
    @Results({
            @Result(property = "id", column = "concept_id"),
            @Result(property = "name", column = "concept_name"),
            @Result(property = "description", column = "concept_description"),
            @Result(property = "chapterId", column = "chapter_id")
            // Concept 实体中没有 chapter_id_fk, course_id, chapter_title, chapter_sequence这些字段
            // 这些字段是辅助用于Service层进行分组的，不会直接映射到Concept实体
    })
    List<Concept> getConceptsWithChapterInfoByCourse(@Param("courseId") int courseId);

    // 获取所有章节的详细信息，用于分组。
    // 这个方法可以复用 getChaptersByCourse，或者专门定义一个。
    // 为了 getConceptsGroupedByChapter 的方便，这里直接获取所有相关章节
    @Select("SELECT id, course_id, title, sequence  FROM chapters WHERE course_id = #{courseId} ORDER BY sequence ASC")
    List<Chapter> findChaptersForGrouping(@Param("courseId") int courseId);

    @Delete("DELETE FROM concepts WHERE course_id = #{courseId}")
    int deleteByCourseId(int courseId);

    @Select("SELECT * FROM courses ORDER BY created_at DESC")
    PaginationResult<Course> getCourses(int pageNum, int pageSize);

    @Select("SELECT " +
            "c.id, " +
            "c.name, " +
            "c.description AS course_description, " +
            "c.teacher_id, " +
            "c.status_student,"+
            "u.name AS teacher_real_name " +
            "FROM courses c " +
            "JOIN users u ON c.teacher_id = u.id " +
            "JOIN course_user cu ON c.id = cu.course_id " +
            "WHERE cu.user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "course_description"),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "statusStudent", column = "status_student"),
            @Result(property = "teacherRealName", column = "teacher_real_name")
    })
    List<CoursesDTO> getMyCourses(@Param("userId") int userId);


    // 此外，你可能还需要一个单独的方法来根据 ID 获取教师的名字，以备不时之需
    @Select("SELECT name FROM users WHERE id = #{userId}")
    String getUserNameById(@Param("userId") int userId);

    // 查询我没选过的所有课程
    @Select("SELECT " +
            "c.id, " +
            "c.name, " +
            "c.description, " +
            "c.status_self AS statusSelf, " +        // 新增字段映射
            "c.status_student AS statusStudent, " +  // 新增字段映射
            "c.teacher_id AS teacherId, " +
            "tu.name AS teacherRealName " +                 // 从 users 表获取教师姓名
            "FROM " +
            "courses c " +
            "JOIN " +
            "users tu ON c.teacher_id = tu.id " +       // 1. 联合 users 表获取教师姓名
            "JOIN " +
            "course_user tcu ON c.id = tcu.course_id AND tcu.role = 'ROLE_TEACHER' " + // 2. 确保课程有教师角色关联
            "LEFT JOIN " +
            "course_user scu ON c.id = scu.course_id AND scu.user_id = #{userId} AND scu.role = 'ROLE_STUDENT' " + // 3. 检查学生是否已选
            "WHERE " +
            "scu.course_id IS NULL")
    @Results({
            @Result(property = "description", column = "description")
    })
    List<CoursesDTO> getNotMyCourses(@Param("userId") int userId);

    // 教师将课程的 status_student 字段更新为 'completed'
    @Update("UPDATE courses SET status_student = 'completed' WHERE id = #{courseId}")
    int completeCourse(@Param("courseId") int courseId);

    @Select("SELECT id FROM courses WHERE name like concat('%',#{courseName},'%')")
    Integer getCourseIdByCourseName(String courseName);


//    // 教师根据自己的ID来查询自己的课程
//    @Select("SELECT  course_id FROM course_user WHERE user_id =#{userId}")
//    List<CoursesDTO>getCoursesByTeacherId(@Param("userId") int userId);



    // 根据用户ID查询用户已完成的课程
    @Select("SELECT," +
            "cur.user_id, " +
            "cur.course_id," +
            "c.status_student AS StatusStudent," +
            "FROM," +
            "course_user cur," +
            "JOIN, " +
            "courses c ON cur.course_id = c.id," +
            "WHERE," +
            "cur.user_id = #{userId}")
    List<CoursesDTO> getCoursesByUserId(@Param("userId") int userId);


    // 教师根据自己id来查询自己创建的课程的数量
    @Select("SELECT  course_id FROM course_user WHERE user_id =#{userId}" )
    List<Integer> getCoursesCountByTeacherId(@Param("userId") int userId);


}

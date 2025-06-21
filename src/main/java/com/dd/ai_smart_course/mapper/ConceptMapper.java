package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ConceptMapper {

    // 获取所有概念
    @Select("SELECT * FROM concepts")
    List<Concept> getAllConcepts();

    // 根据id获取某个章节的 concept
    @Select("SELECT * FROM concepts WHERE chapter_id = #{chapterId}")
    List<Concept> getConceptsByChapterId(@Param("chapterId") int chapterId);

    // 通过概念名称获取 concept
    @Select("SELECT * FROM concepts WHERE name = #{name}")
    Concept getConceptByName(String name);

    // 添加 concept
    @Insert("INSERT INTO concepts (id, chapter_id, name, description) VALUES (#{id}, #{chapterId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 用于获取自增ID
    int addConcept(Concept concept);

    // 更新 concept
    @Update("UPDATE concepts SET chapter_id = #{chapterId}, name = #{name}, description = #{description} WHERE id = #{id}")
    int updateConcept(Concept concept);

    // 删除 concept
    @Delete("DELETE FROM concepts WHERE id = #{id}")
    int deleteConcept(@Param("id") int id);

    /**
     * 绑定知识点与题目（假设为多对多关系，需要中间表 concept_question_relation）
     */
    @Insert("INSERT INTO concept_question(concept_id, question_id) VALUES(#{conceptId}, #{questionId}) " +
            "ON DUPLICATE KEY UPDATE concept_id = #{conceptId}") // 防止重复插入
    void linkConceptToQuestion(@Param("conceptId") Long conceptId, @Param("questionId") Long questionId);

    /**
     * 获取一个知识点下所有题目 (现在包含 difficulty 字段)
     */
    @Select("SELECT q.id, q.content, q.type, q.difficulty FROM questions q " + // <-- 添加了 q.difficulty
            "JOIN concept_question cq ON q.id = cq.question_id " +
            "WHERE cq.concept_id = #{conceptId}")
    List<Question> getQuestionsByConcept(@Param("conceptId") Long conceptId);

    @Insert("INSERT INTO concept_mastery(user_id, concept_id, mastery_level) " + // ER图中concept_mastery没有last_update_time
            "VALUES(#{userId}, #{conceptId}, #{masteryLevel}) " +
            "ON DUPLICATE KEY UPDATE mastery_level = #{masteryLevel}") // ER图中concept_mastery没有last_update_time
    void updateMasteryLevel(@Param("userId") Long userId, @Param("conceptId") Long conceptId, @Param("masteryLevel") int masteryLevel);

    @Select("SELECT mastery_level FROM concept_mastery WHERE user_id = #{userId} AND concept_id = #{conceptId}")
    Integer getMasteryLevel(@Param("userId") Long userId, @Param("conceptId") Long conceptId);

    /**
     * 获取某用户在课程中的知识点掌握情况 (根据 ER 图，通过 chapters 表关联 course_id)
     */
    @Select("SELECT c.id, c.name, c.description, c.chapter_id, ucm.mastery_level " +
            "FROM concepts c " +
            "LEFT JOIN concept_mastery ucm ON c.id = ucm.concept_id AND ucm.user_id = #{userId} " +
            "JOIN chapters chap ON c.chapter_id = chap.id " + // 根据ER图，通过chapters表的course_id筛选
            "WHERE chap.course_id = #{courseId}")
    List<Map<String, Object>> getUserConceptMasteryByCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 获取用户所有知识点的掌握度（用于推荐算法的基础数据）
     */
    @Select("SELECT concept_id, mastery_level FROM concept_mastery WHERE user_id = #{userId}")
    List<Map<String, Object>> getUserAllConceptMastery(@Param("userId") Long userId);

    /**
     * 获取某个概念的详细信息 (用于推荐算法后获取完整 Concept 对象)
     */
    @Select("SELECT id, name, description, chapter_id FROM concepts WHERE id = #{conceptId}")
    Concept getConceptById(@Param("conceptId") Long conceptId);


    @Delete("DELETE FROM concepts WHERE course_id = #{id}")
    int deleteByCourseId(int id);

    @Select("SELECT id FROM concepts")
    List<Long> findAllConceptIds();

    // 获取某个概念
    @Select("SELECT * FROM concepts WHERE id = #{id}")
    Optional<Concept> findById(Long conceptId);

//    /**
//     * 【需要额外数据库表支持】获取用户在某个课程中的错题对应的概念ID及错题次数
//     * ER图中没有直接的表记录用户对每个问题的对错。
//     * 以下是一个基于“理想的” user_question_attempts 表的假设性SQL。
//     * 如果你没有这个表，此方法将无法正常工作，需要根据实际数据来源进行调整。
//     *
//     * 强烈建议：添加一个 user_question_attempts 表，包含 user_id, question_id, is_correct, attempt_time 等字段。
//     *
//     * 假设 user_question_attempts 表存在，结构为:
//     * CREATE TABLE `user_question_attempts` (
//     * `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
//     * `user_id` BIGINT NOT NULL,
//     * `question_id` BIGINT NOT NULL,
//     * `is_correct` BOOLEAN NOT NULL, -- true表示答对，false表示答错
//     * `attempt_time` DATETIME NOT NULL,
//     * FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
//     * FOREIGN KEY (`question_id`) REFERENCES `questions`(`id`)
//     * );
//     */
//    @Select("SELECT cq.concept_id, COUNT(uqa.id) as wrong_count " +
//            "FROM user_question_attempts uqa " + // 假设有此表
//            "JOIN questions q ON uqa.question_id = q.id " +
//            "JOIN concept_question cq ON q.id = cq.question_id " +
//            "JOIN concepts c ON cq.concept_id = c.id " +
//            "JOIN chapters chap ON c.chapter_id = chap.id " +
//            "WHERE uqa.user_id = #{userId} AND uqa.is_correct = FALSE " + // 只统计答错的
//            "AND chap.course_id = #{courseId} " + // 筛选特定课程的错题
//            "GROUP BY cq.concept_id " +
//            "ORDER BY wrong_count DESC")
//    List<Map<String, Object>> getUserWrongConceptCountsByCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
//
//    /**
//     * 【可选的基于learning_logs的实现】
//     * 如果你不想添加新表，并且 learning_logs 记录了类似 'incorrect_answer_question_X' 的 action_type，可以尝试。
//     * 但这种方式通常不如专门的答题记录表精确和可靠。
//     */
//    @Select("SELECT cq.concept_id, COUNT(ll.id) as wrong_count " +
//            "FROM learning_logs ll " +
//            "JOIN questions q ON ll.target_id = q.id AND ll.target_type = 'question' " + // 假设 target_type='question' 且 target_id 是 question_id
//            "JOIN concept_question cq ON q.id = cq.question_id " +
//            "JOIN concepts c ON cq.concept_id = c.id " +
//            "JOIN chapters chap ON c.chapter_id = chap.id " +
//            "WHERE ll.user_id = #{userId} AND ll.action_type = 'incorrect_answer' " + // 假设有此action_type
//            "AND chap.course_id = #{courseId} " +
//            "GROUP BY cq.concept_id " +
//            "ORDER BY wrong_count DESC")
//    List<Map<String, Object>> getUserWrongConceptCountsByCourse_Alternative(@Param("userId") Long userId, @Param("courseId") Long courseId);
}




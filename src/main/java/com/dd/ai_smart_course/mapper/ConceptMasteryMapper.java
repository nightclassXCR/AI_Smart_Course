package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Concept_mastery;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ConceptMasteryMapper {
    // 根据用户ID和概念ID查找
    @Select("SELECT * FROM concept_mastery WHERE user_id = #{userId} AND concept_id = #{conceptId}")
    Optional<Concept_mastery> findByUserIdAndConceptId(@Param("userId") int userId, @Param("conceptId") int conceptId);
    // 插入
    @Insert("INSERT INTO concept_mastery (user_id, concept_id, mastery_level, last_updated)" +
            "VALUES (#{userId}, #{conceptId}, #{masteryLevel}, #{lastUpdated}")
    void insertConceptMastery(Concept_mastery conceptMastery);
    // 更新
    @Update("UPDATE concept_mastery SET mastery_level= #{masteryLevel}, last_updated = #{lastUpdated} WHERE user_id = #{userId} AND concept_id = #{conceptId}")
    void updateConceptMastery(Concept_mastery conceptMastery);
    // 用于定时任务获取所有记录或特定用户/概念的记录
    @Select("SELECT * FROM concept_mastery")
    List<Concept_mastery> findAllMasteries();
    // 用于获取特定用户
    @Select("SELECT * FROM concept_mastery WHERE user_id = #{userId}")
    List<Concept_mastery> findMasteriesByUserId(@Param("userId") int userId);
}

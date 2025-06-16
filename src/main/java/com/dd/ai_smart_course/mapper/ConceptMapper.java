package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Concept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConceptMapper {

    // 获取所有概念
    @Select("SELECT * FROM concept")
    List<Concept> getAllConcepts();

    // 根据id获取某个章节的 concept
    @Select("SELECT * FROM concept WHERE chapter_id = #{chapterId}")
    List<Concept> getConceptsByChapterId(int chapterId);

    // 通过概念名称获取 concept
    @Select("SELECT * FROM concept WHERE name = #{name}")
    Concept getConceptByName(String name);

    // 添加 concept
    @Insert("INSERT INTO concept (id, chapter_id, name, description) VALUES (#{id}, #{chapterId}, #{name}, #{description})")
    int addConcept(Concept concept);

    // 更新 concept
    @Update("UPDATE concept SET chapter_id = #{chapterId}, name = #{name}, description = #{description} WHERE id = #{id}")
    int updateConcept(Concept concept);

    // 删除 concept
    @Delete("DELETE FROM concept WHERE id = #{id}")
    int deleteConcept(int id);
}

package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.File;
import com.dd.ai_smart_course.entity.Resource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ResourceMapper {



    void insert(Resource resource);

    @Select("SELECT * FROM resources WHERE id = #{id}")
    Resource selectById(int id);

    @Select("SELECT * FROM resources WHERE owner_id = #{id}")
    Resource selectByOwnerId(int id);
    @Delete("DELETE FROM resources WHERE id = #{id}")
    void deleteById(int id);

    List<Resource> selectByFilter(Resource resource);
    
    @Select("SELECT * FROM resources")
    List<Resource> list();

    Integer findIdByNameAndType(String ownerName, String ownerType);

    @Select("SELECT * FROM concepts WHERE chapter_id = #{chapterId}")
    List<Concept> listByChapterId(int chapterId);

    @Select("SELECT * FROM resources WHERE id = #{resourceId}")
    Optional<File> findById(int resourceId);
}

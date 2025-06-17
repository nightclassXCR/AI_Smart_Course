package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Score;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScoreMapper {


    @Select("SELECT * FROM scores WHERE user_id = #{userId}")
    Score getScoreByUserId(int userId);

    @Select("SELECT * FROM scores WHERE id = #{id}")
    Score getById(int id);

    @Select("select * from scores where task_id=#{taskId}")
    List<Score> listByTaskId(int taskId);
    void insertBatch(List<Score> scores);

    void update(Score score);


    void deleteByIds(List<Integer> ids);

    @Delete("DELETE FROM scores WHERE id = #{id}")
    void deleteById(Integer id);
}

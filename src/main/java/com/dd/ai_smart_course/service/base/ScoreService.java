package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.dto.ScoreDTO;
import com.dd.ai_smart_course.entity.Score;

import java.util.List;

public interface ScoreService {
    Score getById(int id);

    void insertBatch(List<Score> score);

    void deleteBatch(List<Integer> ids);

    void update(Score score);

    Score getTaskScore(int taskId);

    List<Score> getUserScores(int userId);

    //获取某门课程的所有学生的平均分（假设每份task权重相等）
    List<ScoreDTO> getFinalScoreByCourseId(int courseId);

    //获取学生的分数分布
    List<Integer> getPureScoreDistribution(List<ScoreDTO>  scores);

    //获取学生的分数分布（比例）
    List<Integer> getScoreDistribution(List<ScoreDTO>  scores);
}

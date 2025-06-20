package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.entity.Score;

import java.util.List;

public interface ScoreService {
    Score getById(int id);

    void insertBatch(List<Score> score);

    void deleteBatch(List<Integer> ids);

    void update(Score score);

    Score getTaskScore(int taskId);

    List<Score> getUserScores(int userId);
}

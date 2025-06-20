package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.service.ScoreService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@Slf4j
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;


    @GetMapping("/score/{id}")
    public Result<Score> getById(@PathVariable int id) {
        log.info("getById: {}", id);
        Score score = scoreService.getById(id);
        return Result.success(score);
    }

    @PostMapping("/score")
    public Result insert(@RequestBody List<Score> scores) {
        log.info("insert: {}", scores);
        scoreService.insertBatch(scores);

        return Result.success();
    }

    @DeleteMapping("/score")
    public Result delete(@RequestBody List<Integer> ids) {
        log.info("delete: {}", ids);
        scoreService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/score")
    public Result update(@RequestBody Score score) {
        log.info("update: {}", score);
        scoreService.update(score);
        return Result.success();
    }

    // 查询任务成绩
    @GetMapping("/tasks/{taskId}/scores")
    public Result<Score> getTaskScores(@PathVariable int taskId) {
        log.info("getTaskScores: {}", taskId);
        Score scores = scoreService.getTaskScore(taskId);
        return Result.success(scores);
    }

    // 查询用户成绩
    @GetMapping("/users/{userId}/scores")
    public Result<List<Score>> getUserScores(@PathVariable int userId) {
        log.info("getUserScores: {}", userId);
        List<Score> scores = scoreService.getUserScores(userId);
        return Result.success(scores);
    }


}

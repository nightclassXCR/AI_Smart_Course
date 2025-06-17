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


    @GetMapping("/{id}")
    public Result<Score> getById(@PathVariable int id) {
        log.info("getById: {}", id);
        Score score = scoreService.getById(id);
        return Result.success(score);
    }

    @PostMapping
    public Result insert(@RequestBody List<Score> scores) {
        log.info("insert: {}", scores);
        scoreService.insertBatch(scores);

        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestBody List<Integer> ids) {
        log.info("delete: {}", ids);
        scoreService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Score score) {
        log.info("update: {}", score);
        scoreService.update(score);
        return Result.success();
    }


}

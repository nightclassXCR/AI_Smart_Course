package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.event.LearningActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.time.LocalDateTime;
import java.util.Map;



@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private DifyService difyService;
    @Autowired
    private ApplicationEventPublisher eventPublisher; // 注入事件发布器

    @PostMapping("/ask")
    public Result<Map<String, String>> askDify(@RequestBody Map<String, String> request, @AuthenticationPrincipal User currentUser) {
        Long userId = getCurrentUserId(currentUser);
        String question = request.get("question");

        // ... 调用 DifyService 获取答案 ...
        String answer = difyService.askQuestion(question);

        // 发布学习日志事件
        LearningLog log = new LearningLog();
        log.setUserId(userId);
        log.setTargetType("QNA_SYSTEM"); // 目标类型为问答系统
        log.setTargetId(null); // 或者如果Dify能返回某个知识点ID，可以设置
        log.setActionType("ASK_DIFY"); // 行为类型：Dify 提问
        log.setActionTime(LocalDateTime.now());
        log.setDetail("{\"question\": \"" + question + "\", \"answer\": \"" + answer + "\"}");

        eventPublisher.publishEvent(new LearningActionEvent(this, log));

        return Result.success(Map.of("answer", answer));
    }
}

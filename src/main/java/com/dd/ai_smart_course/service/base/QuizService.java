package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.dto.request.UserQuizSubmissionRequest;

public interface QuizService {
    void processUserQuizSubmission(UserQuizSubmissionRequest  request);
}

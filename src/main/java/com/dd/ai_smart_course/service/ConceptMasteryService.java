package com.dd.ai_smart_course.service;

public interface ConceptMasteryService {

    void recalculateAllMasteryLevels();
    int calculateMasteryForUserConcept(Long userId, Long conceptId);
    void updateMasteryForTaskSubmission(Long userId, Long taskId);
}

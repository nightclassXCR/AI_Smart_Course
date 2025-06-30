package com.dd.ai_smart_course.service.base;

public interface ConceptMasteryService {

    void recalculateAllMasteryLevels();
    int calculateMasteryForUserConcept(int userId, int conceptId);
    void updateMasteryForTaskSubmission(int userId, int taskId);
}

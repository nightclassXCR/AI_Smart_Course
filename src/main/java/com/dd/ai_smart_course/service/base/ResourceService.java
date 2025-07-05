package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.Resource;

import java.util.List;

public interface ResourceService {

    void save(Resource resource);
    Resource findById(int id);
    void delete(int id);
    List<Resource> filter(Resource resource);

    List<Resource> list();

    List<Resource> listByChapterId(int chapterId);

    void viewOrPlayResource(int resourceId, int userId, Integer durationSeconds, String currentProgress, String resourceType);
}

package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.Resource;

import java.util.List;

public interface ResourceService {

    void save(Resource resource);
    Resource findById(Long id);
    void delete(Long id);
    List<Resource> filter(Resource resource);

    Resource list();
}

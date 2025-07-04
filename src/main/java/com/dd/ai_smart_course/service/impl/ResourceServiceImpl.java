package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.File;
import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.ResourceMapper;
import com.dd.ai_smart_course.service.base.ResourceService;
import com.dd.ai_smart_course.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void save(Resource resource) {
        resource.setCreatedAt(LocalDateTime.now());
        int userId = BaseContext.getCurrentId();
        log.info("当前用户ID：{}", userId);
        //TODO 获取当前用户ID
        resource.setUserId(userId);
        resource.setCreatedAt(LocalDateTime.now());
        resource.setUpdatedAt(LocalDateTime.now());

        resourceMapper.insert(resource);
    }

    @Override
    public Resource findById(int id) {
        return resourceMapper.selectById(id);
    }

    @Override
    public void delete(int id) {
        resourceMapper.deleteById(id);
    }

    @Override
    public List<Resource> filter(Resource resource) {
        return resourceMapper.selectByFilter(resource);
    }

    @Override
    public List<Resource> list() {
        return resourceMapper.list();
    }

    @Override
    public List<Resource> listByChapterId(int chapterId) {
        List<Concept> concepts = resourceMapper.listByChapterId(chapterId);
        if (concepts.isEmpty()) {
            return null;
        }
        List<Resource> resources=new ArrayList<>();
        for(Concept concept:concepts) {
            int resourceId =resourceMapper.findIdByNameAndType(concept.getName(), "concept");
            Resource resource = resourceMapper.selectById(resourceId);
            resources.add(resource);
        }
        return resources;
    }

    @Override
    @Transactional
    public void viewOrPlayResource(int resourceId, int userId, Integer durationSeconds, String currentProgress, String resourceType) {
        Optional<File> resourceOptional = resourceMapper.findById(resourceId);
        if (resourceOptional.isEmpty()) {
            throw new IllegalArgumentException("Resource not found: " + resourceId);
        }

        System.out.println("User " + userId + " viewed resource " + resourceId + " for " + durationSeconds + " seconds.");

        String actionType = "view"; // 默认是 'view'
        if (resourceType != null && (resourceType.equalsIgnoreCase("video"))) {
            actionType = "play"; // 如果是视频/音频，使用 'play'
        }

        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "resourse",
                resourceId,
                actionType,   // actionType
                durationSeconds,
                "{\"progress\":\"" + (currentProgress != null ? currentProgress : "N/A") + "\", \"resourceType\":\"" + resourceType + "\"}" // detail
        ));
    }


}

package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.mapper.ResourceMapper;
import com.dd.ai_smart_course.service.base.ResourceService;
import com.dd.ai_smart_course.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public void save(Resource resource) {
        resource.setCreatedAt(LocalDateTime.now());
        int ownerId = BaseContext.getCurrentId();
        log.info("当前用户ID：{}", ownerId);
        //TODO 获取当前用户ID
        resource.setOwnerId(ownerId);
        resource.setOwnerType(Resource.OwnerType.task);
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


}

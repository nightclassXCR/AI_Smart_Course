package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.File;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.FileMapper;
import com.dd.ai_smart_course.service.base.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FileImpl implements FileService {


    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<File> getAllFiles() {
        return fileMapper.getAllFiles();
    }

    @Override
    public File getFileById(int id) {
        return fileMapper.getFileById(id);
    }

    @Override
    public int addFile(File file) {
        return fileMapper.addFile(file);
    }

    @Override
    public int updateFile(File file) {
        return fileMapper.updateFile(file);
    }

    @Override
    public int deleteFile(int id) {
        return fileMapper.deleteFile(id);
    }

//    /**
//     * 用户查看或持续观看资源的方法。
//     *
//     * @param resourceId        被查看的资源ID
//     * @param userId            操作用户ID
//     * @param durationSeconds   用户在当前会话中或某段时间内查看该资源的持续时长 (秒)。
//     * @param currentProgress   用户在资源中的当前进度 (例如视频播放到哪里，JSON字符串)
//     * @param resourceType      资源的具体类型（例如 "VIDEO", "AUDIO", "DOCUMENT"）
//     */



}

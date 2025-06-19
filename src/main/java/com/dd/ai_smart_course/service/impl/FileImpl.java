package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.File;
import com.dd.ai_smart_course.mapper.FileMapper;
import com.dd.ai_smart_course.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileImpl implements FileService {


    @Autowired
    private FileMapper fileMapper;

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

}

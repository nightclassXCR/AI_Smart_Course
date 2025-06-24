package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.File;

import java.util.List;

public interface FileService {
    // 获取所有文件
    List<File> getAllFiles();
    // 获取文件详情
    File getFileById(int id);
    // 添加文件
    int addFile(File file);
    // 更新文件信息
    int updateFile(File file);
    // 删除文件
    int deleteFile(int id);
}

package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.QA;

import java.util.List;

public interface QAService {
    // 获取所有QA
    List<QA> getAllQA();
    // 获取QA详情
    QA getQAById(int id);
    // 添加QA
    int addLog(QA qa);
    // 更新QA信息
    int updateQA(QA qa);
    // 删除QA
    int deleteQA(int id);
}

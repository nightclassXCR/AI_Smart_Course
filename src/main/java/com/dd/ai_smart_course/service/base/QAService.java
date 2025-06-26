package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.QA;
import com.dd.ai_smart_course.service.exception.BusinessException;

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

    //检验关键数据是否非法
    //若用户ID、问题内容非法，则抛出对应异常
    void checkFactor(QA qa) throws BusinessException;

    //根据QAId检查qa是否存在
    //针对 update
    void checkQAExists(Integer qaId) throws BusinessException;
}

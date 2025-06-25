package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.QA;
import com.dd.ai_smart_course.mapper.QAMapper;
import com.dd.ai_smart_course.service.base.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QAImpl implements QAService {
    @Autowired
    private QAMapper qaMapper;

    @Override
    public List<QA> getAllQA() {
        return qaMapper.getAllQAs();
    }

    @Override
    public QA getQAById(int id) {
        return qaMapper.getQAById(id);
    }

    @Override
    public int addLog(QA qa) {
        return qaMapper.addQA(qa);
    }

    @Override
    public int updateQA(QA qa) {
        return qaMapper.updateQA(qa);
    }

    @Override
    public int deleteQA(int id) {
        return qaMapper.deleteQA(id);
    }
}

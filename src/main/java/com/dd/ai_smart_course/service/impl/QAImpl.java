package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.QA;
import com.dd.ai_smart_course.mapper.QAMapper;
import com.dd.ai_smart_course.service.base.QAService;
import com.dd.ai_smart_course.service.base.UserService;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QAImpl implements QAService {
    @Autowired
    private QAMapper qaMapper;

    @Autowired
    private UserImpl userService;

    @Override
    public List<QA> getAllQA() {
        return qaMapper.getAllQAs();
    }

    @Override
    public QA getQAById(int id) {
        return qaMapper.getQAById(id);
    }

    @Override
    public int addLog(QA qa) throws BusinessException{
        try{
            checkFactor(qa);
            return qaMapper.addQA(qa);
        } catch (BusinessException be){
            log.error("can't add in \"qa_records\" table: " + be.getMessage());
            throw be;
        } catch (Exception e){
            log.error("can't add in \"qa_records\" table: " + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int updateQA(QA qa) throws BusinessException{
        try {
            checkQAExists(qa.getId());
            checkFactor(qa);
            return qaMapper.updateQA(qa);
        }catch (BusinessException be){
            log.error("can't update in \"qa_records\" table: " + be.getMessage());
            throw be;
        } catch (Exception e){
            log.error("can't update in \"qa_records\" table: " + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int deleteQA(int id) {
        return qaMapper.deleteQA(id);
    }

    //检验关键数据是否非法
    //若用户ID、问题内容非法，则抛出对应异常
    //针对 add, update
    @Override
    public void checkFactor(QA qa) throws BusinessException {
        String questionText = qa.getQuestionText();

        //检查问题内容是否为空
        if(questionText == null){
            throw new BusinessException(ErrorCode.QA_QUESTION_TEXT_NULL);
        }

        //检查用户ID对应的用户是否存在
        userService.checkUserExists(qa.getUserId());
    }

    //根据QAId检查qa是否存在
    //针对 update
    @Override
    public void checkQAExists(int qaId) throws BusinessException{
        if(qaId == 0){
            throw new BusinessException(ErrorCode.QA_ID_NULL);
        }
        if(qaMapper.getQAById(qaId) == null){
            throw new BusinessException(ErrorCode.QA_NOT_EXISTS);
        }
    }
}

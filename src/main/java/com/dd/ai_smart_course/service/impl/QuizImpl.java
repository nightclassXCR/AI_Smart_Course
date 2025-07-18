package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.SubmissionQuestion;
import com.dd.ai_smart_course.dto.request.UserQuizSubmissionRequest;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.ConceptMastery;
import com.dd.ai_smart_course.mapper.ConceptMapper;
import com.dd.ai_smart_course.mapper.ConceptMasteryMapper;
import com.dd.ai_smart_course.service.base.QuizService;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Objects;

@Service
public class QuizImpl implements QuizService {

    @Autowired
    private ConceptMapper conceptMapper;
    @Autowired
    private ConceptMasteryMapper conceptMasteryMapper;

    private static final int INITIAL_MASTERY_LEVEL = 3;
    private static final int MAX_MASTERY_LEVEL = 5;
    private static final int MIN_MASTERY_LEVEL = 1;


    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public void processUserQuizSubmission(UserQuizSubmissionRequest request) {
        Integer userId = request.getUserId();
        System.out.println("\n--- 评估用户 " + userId + " 的一次性问卷结果 ---");

        if (request.getSubmittedQuestions() == null || request.getSubmittedQuestions().isEmpty()) {
            System.out.println("用户没有作答任何题目，无需评估。");
            return;
        }

        for (SubmissionQuestion submittedQuestion : request.getSubmittedQuestions()) {
            String questionDifyId = submittedQuestion.getId(); // Dify生成的题目ID，仅用于日志
            String questionText = submittedQuestion.getQuestion_text(); // 题目内容
            String correctAnswer = submittedQuestion.getCorrect_answer(); // 正确答案
            String explanation = submittedQuestion.getExplanation(); // 知识点
            String userAnswer = submittedQuestion.getUserAnswer(); // 用户作答

            if (Objects.isNull(explanation) || explanation.isEmpty()) {
                System.out.println("  - 警告: 题目 '" + questionDifyId + "' (题干: '" + questionText + "') 未指定知识点，跳过此题评估。");
                continue;
            }
            if (Objects.isNull(correctAnswer) || correctAnswer.isEmpty()) {
                System.out.println("  - 警告: 题目 '" + questionDifyId + "' (题干: '" + questionText + "') 未指定正确答案，跳过此题评估。");
                continue;
            }

            // 1. 获取或创建概念ID (概念是持久化的，但只存储名称和ID)
            Integer conceptId = findOrCreateConceptId(explanation);

            if (conceptId == null) {
                System.out.println("  - 错误: 无法为概念 '" + explanation + "' 获取ID，跳过题目 '" + questionDifyId + "' 评估。");
                continue;
            }

            // 2. 判断对错 (直接在内存中进行判断)
            boolean isCorrect = userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
            System.out.println("  - 题目DifyID '" + questionDifyId + "' (知识点: '" + explanation + "'): 用户答案 '" + userAnswer + "', 正确答案 '" + correctAnswer + "' -> " + (isCorrect ? "正确" : "错误"));

            // 3. 更新知识点掌握度
            updateConceptMasteryLevel(userId, conceptId, isCorrect);
        }
        System.out.println("用户 " + userId + " 的一次性问卷评估完成。");

    }
    private Integer findOrCreateConceptId(String conceptName) {
        Concept matchedConcept = conceptMapper.findConceptByNameFuzzy(conceptName);
        if (matchedConcept != null) {
            return matchedConcept.getId();
        } else {
//            Concept newConcept = new Concept(conceptName);
//            try {
//                conceptMapper.insertConcept(newConcept);
//                return newConcept.getId();
//            } catch (Exception e) {
//                if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("for key 'name'")) {
//                    Concept existingConcept = conceptMapper.findConceptByNameFuzzy(conceptName);
//                    if (existingConcept != null) {
//                        return existingConcept.getId();
//                    }
//                }
//                System.err.println("创建概念 '" + conceptName + "' 时发生错误: " + e.getMessage());
//                return null;
            //}
            return null;
        }
    }

    /**
     * 根据对错更新用户对某个知识点的掌握度
     */
    private void updateConceptMasteryLevel(Integer userId, Integer conceptId, boolean isCorrect) {
        ConceptMastery conceptMastery = conceptMasteryMapper.findConceptMasteryByUserIdAndConceptId(userId, conceptId);

        int currentMasteryLevel;
        if (conceptMastery == null) {
            currentMasteryLevel = INITIAL_MASTERY_LEVEL;
            System.out.println("    > 创建新掌握记录 (初始掌握度: " + INITIAL_MASTERY_LEVEL + ")");
            conceptMastery = new ConceptMastery(userId, conceptId, currentMasteryLevel);
            conceptMasteryMapper.insertConceptMastery(conceptMastery);
        } else {
            currentMasteryLevel = conceptMastery.getMasteryLevel();
            System.out.println("    > 现有掌握度: " + currentMasteryLevel);
        }

        int masteryChange = 1; // 默认每次掌握度变化1级

        if (isCorrect) {
            currentMasteryLevel = Math.min(currentMasteryLevel + masteryChange, MAX_MASTERY_LEVEL);
            System.out.println("    > 回答正确，掌握度提升至: " + currentMasteryLevel);
        } else {
            currentMasteryLevel = Math.max(currentMasteryLevel - masteryChange, MIN_MASTERY_LEVEL);
            System.out.println("    > 回答错误，掌握度降低至: " + currentMasteryLevel);
        }

        conceptMastery.setMasteryLevel(currentMasteryLevel);
        conceptMasteryMapper.updateConceptMastery(conceptMastery);
        System.out.println("    > 用户 " + userId + " 对概念 " + conceptId + " 的掌握度更新成功。");
    }

//    // 用于查看持久化的概念和掌握度数据，这些方法仍然有用
//    public List<Concept> getAllConcepts() {
//        return conceptMapper.findAllConcepts();
//    }

    public List<ConceptMastery> getAllConceptMasteries() {
        return conceptMasteryMapper.findAllConceptMasteries();
    }

}

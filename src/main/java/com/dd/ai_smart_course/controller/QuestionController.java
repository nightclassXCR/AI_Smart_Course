// package com.dd.ai_smart_course.controller;

// import com.dd.ai_smart_course.dto.QuestionDTO;
// import com.dd.ai_smart_course.entity.Question;
// import com.dd.ai_smart_course.service.base.QuestionService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/questions")
// @Slf4j
// public class QuestionController {

//     @Autowired
//     private QuestionService questionService;

//     @PostMapping
//     public ResponseEntity<String> addQuestion(@RequestBody QuestionDTO dto) {
//         int id = questionService.addQuestion(dto);
//         return ResponseEntity.ok("Question created with ID: " + id);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Question> getById(@PathVariable int id) {
//         return ResponseEntity.ok(questionService.getQuestionById(id));
//     }

//     @GetMapping
//     public ResponseEntity<List<Question>> getAll() {
//         return ResponseEntity.ok(questionService.getAllQuestions());
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> delete(@PathVariable int id) {
//         questionService.deleteQuestion(id);
//         return ResponseEntity.ok("Deleted question ID: " + id);
//     }
// }

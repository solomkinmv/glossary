package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/practices")
public class PracticeController {

    private final QuizPracticeService quizPracticeService;

    @Autowired
    public PracticeController(QuizPracticeService quizPracticeService) {
        this.quizPracticeService = quizPracticeService;
    }

    @GetMapping("/tests")
    ResponseEntity<Quiz> getTest(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting quiz for word set with id {}", wordSetId);
        return ResponseEntity.ok(quizPracticeService.generateQuiz(wordSetId));
    }

    @PostMapping("/tests")
    ResponseEntity<Void> handleResults(@RequestBody QuizResults quizResults) {
        log.info("Handling quiz results");
        quizPracticeService.handleResults(quizResults);
        return ResponseEntity.ok().build();
    }
}
package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.test.QuizPractice;
import io.github.solomkinmv.glossary.service.practice.test.QuizPracticeService;
import io.github.solomkinmv.glossary.service.practice.test.QuizResults;
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

    @GetMapping("tests")
    ResponseEntity<QuizPractice> getTest(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting test for word set with id {}", wordSetId);
        return ResponseEntity.ok(quizPracticeService.generateQuiz(wordSetId));
    }

    @PostMapping("tests")
    ResponseEntity<Void> handleResults(@RequestBody QuizResults quizResults) {
        log.info("Handling quiz results");
        quizPracticeService.handleResults(quizResults);
        return ResponseEntity.ok().build();
    }
}
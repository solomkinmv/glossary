package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.service.practice.writing.WritingPracticeService;
import io.github.solomkinmv.glossary.service.practice.writing.WritingPracticeTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: create resources for each returned value
 */
@Slf4j
@RestController
@RequestMapping("/api/practices")
public class PracticeController {

    private final QuizPracticeService quizPracticeService;
    private final WritingPracticeService writingPracticeService;

    @Autowired
    public PracticeController(QuizPracticeService quizPracticeService, WritingPracticeService writingPracticeService) {
        this.quizPracticeService = quizPracticeService;
        this.writingPracticeService = writingPracticeService;
    }

    @GetMapping("/quizzes")
    ResponseEntity<Quiz> getQuiz(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting quiz for word set with id {}", wordSetId);
        return ResponseEntity.ok(quizPracticeService.generateTest(wordSetId));
    }

    @GetMapping("/writings")
    ResponseEntity<WritingPracticeTest> getWritingTest(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting writing practice test for word set with id {}", wordSetId);
        return ResponseEntity.ok(writingPracticeService.generateTest(wordSetId));
    }

    @PostMapping("")
    ResponseEntity<Void> handleResults(@RequestBody PracticeResults practiceResults) {
        log.info("Handling quiz results");
        quizPracticeService.handle(practiceResults);
        return ResponseEntity.ok().build();
    }
}
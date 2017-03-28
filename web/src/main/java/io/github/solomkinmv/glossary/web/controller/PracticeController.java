package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.service.practice.writing.WritingPracticeService;
import io.github.solomkinmv.glossary.web.resource.QuizResource;
import io.github.solomkinmv.glossary.web.resource.WritingPracticeTestResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public QuizResource getQuiz(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting quiz for word set with id {}", wordSetId);
        return new QuizResource(quizPracticeService.generateTest(wordSetId), wordSetId);
    }

    @RequestMapping("/writings")
    public WritingPracticeTestResource getWritingTest(@RequestParam("wordSetId") Long wordSetId) {
        log.info("Getting writing practice test for word set with id {}", wordSetId);
        return new WritingPracticeTestResource(writingPracticeService.generateTest(wordSetId), wordSetId);
    }

    @PostMapping("")
    public ResponseEntity<Void> handleResults(@RequestBody PracticeResults practiceResults) {
        log.info("Handling quiz results");
        quizPracticeService.handle(practiceResults);
        return ResponseEntity.ok().build();
    }
}
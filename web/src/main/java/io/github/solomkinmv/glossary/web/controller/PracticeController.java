package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.test.QuizPractice;
import io.github.solomkinmv.glossary.service.practice.test.QuizPracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practices")
public class PracticeController {

    private final QuizPracticeService quizPracticeService;

    @Autowired
    public PracticeController(QuizPracticeService quizPracticeService) {
        this.quizPracticeService = quizPracticeService;
    }

    @GetMapping("")
    ResponseEntity<QuizPractice> getTest(@RequestParam Long wordSetId) {
        return ResponseEntity.ok(quizPracticeService.generateQuiz(wordSetId));
    }
}
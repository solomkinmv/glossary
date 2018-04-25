package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.service.practice.PracticeParameters;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.generic.GenericPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.generic.GenericTest;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.words.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.repetition.RepetitionPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.repetition.RepetitionTest;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/practices")
@AllArgsConstructor
public class PracticeController {

    private final QuizPracticeService quizPracticeService;
    private final WritingPracticeService writingPracticeService;
    private final RepetitionPracticeService repetitionPracticeService;
    private final GenericPracticeService genericPracticeService;
    private final PracticeResultsHandler practiceResultsHandler;

    @GetMapping("/quiz")
    public Quiz getQuiz(@RequestParam("userId") long userId,
                        @RequestParam(value = "setId", required = false) Long setId,
                        @RequestParam("originQuestions") boolean originQuestions) {
        log.info("Getting quiz for word set with id {} [userId: {}]", setId, userId);
        return quizPracticeService.generateTest(userId, new PracticeParameters(setId, originQuestions));
    }

    @GetMapping("/writing")
    public WritingPracticeTest getWritingTest(@RequestParam("userId") long userId,
                                              @RequestParam(value = "setId", required = false) Long setId,
                                              @RequestParam("originQuestions") boolean originQuestions) {
        log.info("Getting writing practice test for word set with id {} [userId: {}]", setId, userId);
        return writingPracticeService.generateTest(userId, new PracticeParameters(setId, originQuestions));
    }

    @GetMapping("/repetition")
    public RepetitionTest getRepetitionTest(@RequestParam("userId") long userId,
                                            @RequestParam(value = "setId", required = false) Long setId) {
        log.info("Getting words for repetition [wordSetId: {}, userId: {}]", setId, userId);
        return repetitionPracticeService.generateTest(userId, new PracticeParameters(setId));
    }

    @GetMapping("/generic")
    public GenericTest getGenericTest(@RequestParam("userId") long userId,
                                      @RequestParam(value = "setId", required = false) Long setId) {
        log.info("Getting words for generic test [wordSetId: {}, userId: {}]", setId, userId);
        return genericPracticeService.generateTest(userId, new PracticeParameters(setId));
    }

    @PostMapping
    public void handleResults(@Validated @RequestBody PracticeResults practiceResults) {
        log.info("Handling quiz results");
        practiceResultsHandler.handle(practiceResults);
    }
}

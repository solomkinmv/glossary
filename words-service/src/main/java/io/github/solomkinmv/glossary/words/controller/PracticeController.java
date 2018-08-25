package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.service.practice.PracticeParameters;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.PracticeType;
import io.github.solomkinmv.glossary.words.service.practice.generic.GenericPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.generic.GenericTest;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.words.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest;
import io.github.solomkinmv.glossary.words.util.OAuth2Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/practices")
@AllArgsConstructor
public class PracticeController {

    private final QuizPracticeService quizPracticeService;
    private final WritingPracticeService writingPracticeService;
    private final GenericPracticeService genericPracticeService;
    private final PracticeResultsHandler practiceResultsHandler;

    @GetMapping("/quiz")
    public Quiz getQuiz(@RequestParam(value = "setId", required = false) Long setId,
                        @RequestParam("originQuestions") boolean originQuestions,
                        OAuth2Authentication authentication) {
        String subjectId = OAuth2Utils.subjectId(authentication);
        log.info("Getting quiz for word set with id {} [subjectId: {}]", setId, subjectId);
        return quizPracticeService.generateTest(subjectId, new PracticeParameters(setId, originQuestions));
    }

    @GetMapping("/writing")
    public WritingPracticeTest getWritingTest(@RequestParam(value = "setId", required = false) Long setId,
                                              @RequestParam("originQuestions") boolean originQuestions,
                                              OAuth2Authentication authentication) {
        String subjectId = OAuth2Utils.subjectId(authentication);
        log.info("Getting writing practice test for word set with id {} [subjectId: {}]", setId, subjectId);
        return writingPracticeService.generateTest(subjectId, new PracticeParameters(setId, originQuestions));
    }

    @GetMapping("/generic")
    public GenericTest getGenericTest(@RequestParam(value = "setId", required = false) Long setId,
                                      @RequestParam("originQuestions") boolean originQuestions,
                                      @RequestParam(value = "practiceType", required = false) PracticeType practiceType,
                                      OAuth2Authentication authentication) {
        String subjectId = OAuth2Utils.subjectId(authentication);
        log.info("Getting words for generic test [wordSetId: {}, subjectId: {}]", setId, subjectId);
        return genericPracticeService.generateTest(subjectId, new PracticeParameters(setId, originQuestions, practiceType));
    }

    @PostMapping
    public void handleResults(@Validated @RequestBody PracticeResults practiceResults) {
        log.info("Handling quiz results");
        practiceResultsHandler.handle(practiceResults);
    }
}

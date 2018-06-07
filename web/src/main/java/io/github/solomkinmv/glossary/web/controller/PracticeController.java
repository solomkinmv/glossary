package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.practice.PracticeParameters;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeService;
import io.github.solomkinmv.glossary.service.practice.repetition.RepetitionPracticeService;
import io.github.solomkinmv.glossary.service.practice.writing.WritingPracticeService;
import io.github.solomkinmv.glossary.web.converter.WordConverter;
import io.github.solomkinmv.glossary.web.resource.QuizResource;
import io.github.solomkinmv.glossary.web.resource.RepetitionResource;
import io.github.solomkinmv.glossary.web.resource.WritingPracticeTestResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
@RequestMapping("/api/practices")
public class PracticeController {

    private final QuizPracticeService quizPracticeService;
    private final WritingPracticeService writingPracticeService;
    private final RepetitionPracticeService repetitionPracticeService;
    private final WordConverter wordConverter;

    @Autowired
    public PracticeController(QuizPracticeService quizPracticeService, WritingPracticeService writingPracticeService,
                              RepetitionPracticeService repetitionPracticeService, WordConverter wordConverter) {
        this.quizPracticeService = quizPracticeService;
        this.writingPracticeService = writingPracticeService;
        this.repetitionPracticeService = repetitionPracticeService;
        this.wordConverter = wordConverter;
    }

    @RequestMapping(value = "/quizzes", method = GET)
    public QuizResource getQuiz(
            @CurrentUser AuthenticatedUser user,
            @RequestParam(value = "setId", required = false) Long setId,
            @RequestParam("originQuestions") boolean originQuestions) {
        log.info("Getting quiz for word set with id {}", setId);
        return new QuizResource(
                quizPracticeService.generateTest(user.getUsername(), new PracticeParameters(setId, originQuestions)),
                setId, originQuestions);
    }

    @RequestMapping(value = "/writings", method = GET)
    public WritingPracticeTestResource getWritingTest(
            @CurrentUser AuthenticatedUser user,
            @RequestParam(value = "setId", required = false) Long setId,
            @RequestParam("originQuestions") boolean originQuestions) {
        log.info("Getting writing practice test for word set with id {}", setId);
        return new WritingPracticeTestResource(
                writingPracticeService.generateTest(user.getUsername(), new PracticeParameters(setId, originQuestions)),
                setId, originQuestions);
    }

    @RequestMapping(value = "/repetitions", method = GET)
    public RepetitionResource getWordsForRepetition(
            @CurrentUser AuthenticatedUser user,
            @RequestParam(value = "setId", required = false) Long setId) {
        log.info("Getting words for repetition (set id {})", setId);
        return new RepetitionResource(
                repetitionPracticeService.generateTest(user.getUsername(), new PracticeParameters(setId)).stream()
                                         .map(wordConverter::toDto)
                                         .collect(Collectors.toList()),
                setId);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Void> handleResults(@Validated @RequestBody PracticeResults practiceResults) {
        log.info("Handling quiz results");
        quizPracticeService.handle(practiceResults);
        return ResponseEntity.ok().build();
    }
}
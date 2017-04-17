package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.web.controller.PracticeController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class QuizResource extends ResourceSupport {
    private final Quiz quiz;

    public QuizResource(Quiz quiz, Long wordSetId, boolean originQuestions) {
        this.quiz = quiz;

        add(linkTo(methodOn(PracticeController.class).getQuiz(null, wordSetId, originQuestions)).withSelfRel());
        add(linkTo(methodOn(PracticeController.class).getWritingTest(null, wordSetId, originQuestions)).withRel(
                "writingTest"));
        add(linkTo(methodOn(PracticeController.class).handleResults(null)).withRel("handleResults"));
    }
}

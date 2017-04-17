package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.service.practice.writing.WritingPracticeTest;
import io.github.solomkinmv.glossary.web.controller.PracticeController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class WritingPracticeTestResource extends ResourceSupport {
    private final WritingPracticeTest writingPracticeTest;

    public WritingPracticeTestResource(WritingPracticeTest writingPracticeTest, long wordSetId, boolean originQuestions) {
        this.writingPracticeTest = writingPracticeTest;

        add(linkTo(methodOn(PracticeController.class).getWritingTest(null, wordSetId, originQuestions)).withSelfRel());
        add(linkTo(methodOn(PracticeController.class).getQuiz(null, wordSetId, originQuestions)).withRel("quiz"));
        add(linkTo(methodOn(PracticeController.class).handleResults(null)).withRel("handleResults"));

    }
}

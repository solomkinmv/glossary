package io.github.solomkinmv.glossary.words.service.practice;

import lombok.Value;

import static io.github.solomkinmv.glossary.words.service.practice.PracticeType.LEARNING;

@Value
public class PracticeParameters {

    Long wordSetId;
    boolean originQuestions;
    PracticeType practiceType;

    public PracticeParameters(Long wordSetId, Boolean originQuestions) {
        this(wordSetId, originQuestions, LEARNING);
    }

    public PracticeParameters(Long wordSetId, boolean originQuestions, PracticeType practiceType) {
        this.wordSetId = wordSetId;
        this.originQuestions = originQuestions;
        this.practiceType = practiceType;
    }
}

package io.github.solomkinmv.glossary.words.service.practice;

import lombok.Value;

@Value
public class PracticeParameters {

    Long wordSetId;
    boolean originQuestions;

    public PracticeParameters(Long wordSetId) {
        this(wordSetId, true);
    }

    public PracticeParameters(Long wordSetId, Boolean originQuestions) {
        this.wordSetId = wordSetId;
        this.originQuestions = originQuestions;
    }
}

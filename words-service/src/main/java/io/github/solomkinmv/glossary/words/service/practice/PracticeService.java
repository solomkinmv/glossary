package io.github.solomkinmv.glossary.words.service.practice;

public interface PracticeService<T> {

    T generateTest(String subjectId, PracticeParameters practiceParameters);
}

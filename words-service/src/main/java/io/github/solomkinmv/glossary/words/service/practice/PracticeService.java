package io.github.solomkinmv.glossary.words.service.practice;

public interface PracticeService<T> {

    T generateTest(long userId, PracticeParameters practiceParameters);
}

package io.github.solomkinmv.glossary.words.service.practice;

public interface PracticeService<T, R> {

    T generateTest(long userId, PracticeParameters practiceParameters);

    void handle(R result);
}

package io.github.solomkinmv.glossary.service.practice;

public interface PracticeService<T, R> {
    T generateTest(String username, PracticeParameters practiceParameters);

    void handle(R result);
}

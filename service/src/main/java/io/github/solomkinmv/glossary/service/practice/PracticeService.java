package io.github.solomkinmv.glossary.service.practice;

public interface PracticeService<T, R> {
    T generateTest(String username, long wordSetId);

    void handle(R result);
}

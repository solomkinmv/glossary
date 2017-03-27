package io.github.solomkinmv.glossary.service.practice.test;

public interface QuizPracticeService {
    QuizPractice generateQuiz(long wordSetId);

    void handleResults(QuizResults results);
    // TODO: create method to consume quiz results
}

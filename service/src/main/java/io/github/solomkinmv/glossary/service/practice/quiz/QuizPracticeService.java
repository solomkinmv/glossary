package io.github.solomkinmv.glossary.service.practice.quiz;

public interface QuizPracticeService {
    Quiz generateQuiz(long wordSetId);

    void handleResults(QuizResults results);
}

package io.github.solomkinmv.glossary.words.service.practice;


import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;

public interface StatusUpdater {

    WordStage compute(WordStage currentStage, boolean correctAnswer);
}

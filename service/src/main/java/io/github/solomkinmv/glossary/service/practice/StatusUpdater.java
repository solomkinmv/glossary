package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.WordStage;

public interface StatusUpdater {
    WordStage compute(WordStage currentStage, boolean correctAnswer);
}

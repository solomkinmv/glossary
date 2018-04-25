package io.github.solomkinmv.glossary.words.service.practice.status;

import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.github.solomkinmv.glossary.words.persistence.domain.WordStage.*;

@Component
public class StatusUpdaterImpl implements StatusUpdater {
    private final Map<Boolean, Map<WordStage, WordStage>> operatorMapping;

    public StatusUpdaterImpl() {
        operatorMapping = Map.of(
                true, Map.of(
                        NOT_LEARNED, LEARNING,
                        LEARNING, LEARNED,
                        LEARNED, LEARNED
                ),
                false, Map.of(
                        NOT_LEARNED, NOT_LEARNED,
                        LEARNING, NOT_LEARNED,
                        LEARNED, NOT_LEARNED
                )
        );
    }

    @Override
    public WordStage compute(WordStage currentStage, boolean correctAnswer) {
        return operatorMapping.get(correctAnswer).get(currentStage);
    }
}

package io.github.solomkinmv.glossary.service.practice;

import com.google.common.collect.ImmutableMap;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.github.solomkinmv.glossary.persistence.model.WordStage.*;

@Component
public class StatusUpdaterImpl implements StatusUpdater {
    private final Map<Boolean, Map<WordStage, WordStage>> operatorMapping;

    public StatusUpdaterImpl() {
        operatorMapping = ImmutableMap.of(
                true, ImmutableMap.of(
                        NOT_LEARNED, LEARNING,
                        LEARNING, LEARNED,
                        LEARNED, LEARNED
                ),
                false, ImmutableMap.of(
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

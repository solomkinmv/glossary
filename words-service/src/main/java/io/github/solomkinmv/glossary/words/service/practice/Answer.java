package io.github.solomkinmv.glossary.words.service.practice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.Value;

@Value
public class Answer {

    long wordId;
    String answerText;
    WordStage stage;
    String image;
    String pronunciation;

    @JsonCreator
    public Answer(@JsonProperty("wordId") long wordId,
                  @JsonProperty("answerText") String answerText,
                  @JsonProperty("stage") WordStage stage,
                  @JsonProperty("image") String image,
                  @JsonProperty("pronunciation") String pronunciation) {
        this.wordId = wordId;
        this.answerText = answerText;
        this.stage = stage;
        this.image = image;
        this.pronunciation = pronunciation;
    }
}

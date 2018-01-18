package io.github.solomkinmv.glossary.service.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeResults {
    @NotNull
    private Map<Long, Boolean> wordAnswers;
}

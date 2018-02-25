package io.github.solomkinmv.glossary.translateserviceclient.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslateResult {

    private String sourceText;
    private List<String> result;
    private Language sourceLanguage;
    private Language targetLanguage;

}

package io.github.solomkinmv.translateservice.controller;

import io.github.solomkinmv.translateservice.domain.Language;
import io.github.solomkinmv.translateservice.domain.TranslateResult;
import io.github.solomkinmv.translateservice.service.Translator;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TranslateController {

    private final Translator translator;

    @Cacheable("translates")
    @PostMapping("/translate")
    public ResponseEntity<TranslateResult> translate(@RequestParam("text") String text,
                                                     @RequestParam(value = "source", required = false) Language source,
                                                     @RequestParam("target") Language translationDirection) {

        if (source == null) {
            return translator.execute(text, translationDirection)
                             .map(ResponseEntity::ok)
                             .orElseGet(ResponseEntity.notFound()::build);
        }

        return translator.execute(text, source, translationDirection)
                         .map(ResponseEntity::ok)
                         .orElseGet(ResponseEntity.notFound()::build);
    }

}

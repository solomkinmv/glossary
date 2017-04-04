package io.github.solomkinmv.glossary.service.search;

import io.github.solomkinmv.glossary.persistence.model.Word;
import org.springframework.stereotype.Service;

@Service
public class SearchConverter {
    public SearchResult.Record toSearchRecord(Word word) {
        return new SearchResult.Record(
                word.getText(),
                word.getTranslations(),
                word.getImages(),
                word.getSound()
        );
    }
}

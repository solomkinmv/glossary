package io.github.solomkinmv.glossary.service.search;

import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.translate.Language;
import io.github.solomkinmv.glossary.service.translate.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
public class SearchServiceImpl implements SearchService {

    private final WordService wordService;
    private final SearchConverter searchConverter;
    private final Translator translator;

    @Autowired
    public SearchServiceImpl(WordService wordService, SearchConverter searchConverter, Translator translator) {
        this.wordService = wordService;
        this.searchConverter = searchConverter;
        this.translator = translator;
    }

    @Override
    public SearchResult executeSearch(String text) {
        List<Word> similarWords = wordService.search(text);
        if (similarWords.isEmpty()) {
            return prepareTheOnlySuggestion(text);
        }

        Comparator<SearchResult.Record> byText = (r1, r2) -> r1.getText().compareToIgnoreCase(r2.getText());

        return new SearchResult(similarWords.stream()
                                            .map(searchConverter::toSearchRecord)
                                            .sorted(byText)
                                            .collect(Collectors.toList()));
    }

    private SearchResult prepareTheOnlySuggestion(String text) {
        List<String> singleTranslation = translator.execute(text, Language.RUSSIAN, Language.ENGLISH)
                                                   .map(Collections::singletonList)
                                                   .orElse(Collections.emptyList());
        return new SearchResult(
                singletonList(new SearchResult.Record(
                        text,
                        singleTranslation,
                        null,
                        null
                ))
        );
    }
}

package io.github.solomkinmv.glossary.service.search;

import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.translate.Language;
import io.github.solomkinmv.glossary.service.translate.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * Search tries to find word by query or translate specified text and then adds
     * info about similar words till the {@link #SEARCH_LIMIT}.
     */
    @Override
    public SearchResult executeSearch(String text) {
        Comparator<SearchResult.Record> byText = (r1, r2) -> r1.getText().compareToIgnoreCase(r2.getText());
        List<SearchResult.Record> similarWords = wordService.search(text)
                                                            .stream()
                                                            .map(searchConverter::toSearchRecord)
                                                            .sorted(byText)
                                                            .limit(SearchService.SEARCH_LIMIT)
                                                            .collect(Collectors.toList());

        if (containsText(similarWords, text)) {
            return new SearchResult(similarWords);
        }

        return new SearchResult(insertTranslatedRecord(similarWords, text));
    }

    private List<SearchResult.Record> insertTranslatedRecord(List<SearchResult.Record> similarWords, String text) {
        Optional<SearchResult.Record> translatedRecord = prepareTranslatedRecord(text);
        if (translatedRecord.isPresent()) {
            similarWords.add(0, translatedRecord.get());
            similarWords.remove(similarWords.size() - 1);
        }
        return similarWords;
    }

    private boolean containsText(List<SearchResult.Record> similarWords, String text) {
        return similarWords.stream().anyMatch(record -> record.getText().equals(text));
    }

    private Optional<SearchResult.Record> prepareTranslatedRecord(String text) {
        Function<String, SearchResult.Record> translationToRecord = translation ->
                new SearchResult.Record(
                        text,
                        Collections.singleton(translation),
                        Collections.emptySet(),
                        null
                );
        return translator.execute(text, Language.ENGLISH, Language.RUSSIAN)
                         .map(translationToRecord);
    }
}

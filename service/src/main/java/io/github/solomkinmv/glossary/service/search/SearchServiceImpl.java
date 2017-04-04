package io.github.solomkinmv.glossary.service.search;

import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.translate.Language;
import io.github.solomkinmv.glossary.service.translate.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Optional<SearchResult.Record> wordFromDb = wordService.findByText(text)
                                                              .map(searchConverter::toSearchRecord);
        Optional<SearchResult.Record> translatedWord = prepareTranslatedRecord(text);

        // generate stream for the first elements
        // if word from db and translated word
        Stream<SearchResult.Record> initialStream;
        if (wordFromDb.isPresent() && translatedWord.isPresent()) {
            initialStream = generateInitialStream(wordFromDb.get(), translatedWord.get());
        } else {
            initialStream = Stream.concat(wordFromDb.map(Stream::of).orElseGet(Stream::empty),
                                          translatedWord.map(Stream::of).orElseGet(Stream::empty));
        }

        Comparator<SearchResult.Record> byText = (r1, r2) -> r1.getText().compareToIgnoreCase(r2.getText());
        Stream<SearchResult.Record> similarWords = wordService.search(text)
                                                              .stream()
                                                              .limit(SearchService.SEARCH_LIMIT)
                                                              .map(searchConverter::toSearchRecord)
                                                              .sorted(byText);

        return new SearchResult(
                Stream.concat(initialStream, similarWords)
                      .distinct()
                      .limit(SearchService.SEARCH_LIMIT)
                      .collect(Collectors.toList()));
    }

    private Stream<SearchResult.Record> generateInitialStream(SearchResult.Record wordFromDb, SearchResult.Record translatedWord) {
        if (wordFromDb.getTranslations().equals(translatedWord.getTranslations())) {
            return Stream.of(wordFromDb);
        }
        return Stream.of(wordFromDb, translatedWord);
    }

    private Optional<SearchResult.Record> prepareTranslatedRecord(String text) {
        Function<String, SearchResult.Record> translationToRecord = translation ->
                new SearchResult.Record(
                        text,
                        Collections.singletonList(translation),
                        Collections.emptyList(),
                        null
                );
        return translator.execute(text, Language.ENGLISH, Language.RUSSIAN)
                         .map(translationToRecord);
    }
}

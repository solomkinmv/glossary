package io.github.solomkinmv.glossary.service.statistics;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.domain.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class StatisticGeneratorImpl implements StatisticGenerator {

    private final WordService wordService;

    @Autowired
    public StatisticGeneratorImpl(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public Statistic generate(String username) {
        List<StudiedWord> words = wordService.listByUsername(username);

        long totalWords = words.stream()
                               .map(StudiedWord::getText)
                               .distinct()
                               .count();

        long learnedWords = words.stream()
                                 .filter(distinctByKey(StudiedWord::getText))
                                 .filter(word -> WordStage.LEARNED.equals(word.getStage()))
                                 .count();
        return new Statistic(totalWords, learnedWords);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

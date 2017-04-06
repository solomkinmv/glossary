package io.github.solomkinmv.glossary.web.controller;

import com.google.common.collect.ImmutableMap;
import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.provider.AbstractTestProvider;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@Slf4j
public class PracticeControllerTest extends MockMvcBase {

    private final List<StudiedWord> wordList = new ArrayList<>();
    private final List<WordSet> wordSets = new ArrayList<>();

    @Autowired
    private WordService wordService;
    @Autowired
    private UserDictionaryService userDictionaryService;

    @Before
    public void setUp() throws Exception {
        log.info("Setting up test method");

        StudiedWord word1 = new StudiedWord("word1", "слово1");
        StudiedWord word2 = new StudiedWord("word2", "слово2");
        StudiedWord word3 = new StudiedWord("word3", "слово3");
        StudiedWord word4 = new StudiedWord("word4", "слово4");
        StudiedWord word5 = new StudiedWord("word5", "слово5");
        StudiedWord word6 = new StudiedWord("word6", "слово6");
        StudiedWord word7 = new StudiedWord("word7", "слово7");
        StudiedWord word8 = new StudiedWord("word8", "слово8");
        StudiedWord word9 = new StudiedWord("word9", "слово9");
        StudiedWord word0 = new StudiedWord("word0", "слово0");
        StudiedWord word10 = new StudiedWord("word10", "слово10");
        wordList.add(wordService.save(word1));
        wordList.add(wordService.save(word2));
        wordList.add(wordService.save(word3));
        wordList.add(wordService.save(word4));
        wordList.add(wordService.save(word5));
        wordList.add(wordService.save(word6));
        wordList.add(wordService.save(word7));
        wordList.add(wordService.save(word8));
        wordList.add(wordService.save(word9));
        wordList.add(wordService.save(word0));
        wordList.add(wordService.save(word10));

        wordService.updateWordMeta(word2, WordStage.LEARNING, null);
        wordService.updateWordMeta(word3, WordStage.LEARNING, null);
        wordService.updateWordMeta(word4, WordStage.LEARNED, null);
        wordService.updateWordMeta(word5, WordStage.LEARNED, null);

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        WordSet ws1 = new WordSet("ws1", "desc",
                                  Arrays.asList(word1, word2, word3, word4, word5, word6, word7, word8, word9, word0,
                                                word10));
        WordSet ws2 = new WordSet("ws2", "desc", null);

        wordSets.add(ws1);
        wordSets.add(ws2);

        Set<WordSet> userOneSet = new HashSet<>();
        userOneSet.add(ws1);
        Set<WordSet> userTwoSet = new HashSet<>();
        userTwoSet.add(ws2);

        UserDictionary dict1 = new UserDictionary(userOneSet, user1);
        UserDictionary dict2 = new UserDictionary(userTwoSet, user2);

        userDictionaryService.save(dict1);
        userDictionaryService.save(dict2);
    }

    @Test
    public void getQuizWorksProperly() throws Exception {
        mockMvc.perform(get("/api/practices/quizzes")
                                .param("setId", wordSets.get(0).getId().toString())
                                .with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.quiz.questions", hasSize(AbstractTestProvider.TEST_SIZE)))
               .andExpect(jsonPath("$.quiz.questions[*].alternatives", everyItem(hasSize(5))))
               .andExpect(jsonPath("$.quiz.questions[?(@.questionText=='word1')].answer.answerText",
                                   containsInAnyOrder(wordList.get(0).getTranslation())));
    }

    @Test
    public void handlesTestResults() throws Exception {

        PracticeResults results = new PracticeResults(
                ImmutableMap.<Long, Boolean>builder()
                        .put(wordList.get(0).getId(), true)
                        .put(wordList.get(1).getId(), true)
                        .put(wordList.get(2).getId(), false)
                        .put(wordList.get(3).getId(), true)
                        .put(wordList.get(4).getId(), false)
                        .put(wordList.get(5).getId(), false)
                        .build());

        mockMvc.perform(post("/api/practices")
                                .content(jsonConverter.toJson(results))
                                .contentType(contentType)
                                .with(userToken()))
               .andExpect(status().isOk());

        assertEquals(WordStage.LEARNING, getStageByWordIndex(0));
        assertEquals(WordStage.LEARNED, getStageByWordIndex(1));
        assertEquals(WordStage.NOT_LEARNED, getStageByWordIndex(2));
        assertEquals(WordStage.LEARNED, getStageByWordIndex(3));
        assertEquals(WordStage.NOT_LEARNED, getStageByWordIndex(4));
        assertEquals(WordStage.NOT_LEARNED, getStageByWordIndex(5));
    }

    private WordStage getStageByWordIndex(int index) {
        return wordService.getById(wordList.get(index).getId()).map(StudiedWord::getStage).orElse(null);
    }
}
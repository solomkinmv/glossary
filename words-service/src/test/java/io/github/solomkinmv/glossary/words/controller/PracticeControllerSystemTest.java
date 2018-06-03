package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PracticeControllerSystemTest extends BaseTest {

    private WordSetResponse wordSet;

    @MockBean
    private TtsClient ttsClient;

    @Before
    public void mockTts() {
        when(ttsClient.getSpeech(anyString()))
                .thenReturn(new SpeechResult("speech-url"));
    }

    @Before
    public void setUp() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);


        IntStream.rangeClosed(1, 10)
                 .mapToObj(i -> new WordMeta("word" + i, "translation" + i, "img-url"))
                 .forEach(word -> addWordToWordSet(wordSetId, word));

        wordSet = getWordSetById(wordSetId);
    }

    @Test
    public void handlesTestResults() throws Exception {
        WordResponse word0 = wordSet.getWords().get(0);
        WordResponse word1 = wordSet.getWords().get(1);
        WordResponse word2 = wordSet.getWords().get(2);

        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        WordSetResponse updatedWordSet = getWordSetById(wordSet.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(2).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 2
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        updatedWordSet = getWordSetById(wordSet.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNED);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 3
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        updatedWordSet = getWordSetById(wordSet.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.NOT_LEARNED);
    }

    @Test
    public void generatesGenericTest() throws Exception {
        mockMvc.perform(get("/practices/generic")
                                .param("userId", valueOf(userId.get())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.words", hasSize(AbstractTestProvider.TEST_SIZE)));
    }
}
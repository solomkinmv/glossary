package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.repository.WordRepository;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static io.github.solomkinmv.glossary.words.persistence.domain.WordStage.NOT_LEARNED;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WordSetControllerSystemTest extends BaseTest {

    @Autowired
    private WordSetService wordSetService;
    @Autowired
    private WordRepository wordRepository;
    @MockBean
    private TtsClient ttsClient;

    @Before
    public void setUpMockMvc() {
        super.setUpMockMvc();

        when(ttsClient.getSpeech(any(String.class)))
                .thenReturn(new SpeechResult("url"));
    }

    @Test
    public void createsWordSet() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);

        long wordSetId = createWordSet(wordSetMeta);
        WordSet wordSet = wordSetService.getWordSet(wordSetId);

        assertThat(wordSet.getId()).isEqualTo(wordSetId);
        assertThat(wordSet.getUserId()).isEqualTo(userId.get());
        assertThat(wordSet.getName()).isEqualTo(name);
        assertThat(wordSet.getDescription()).isEqualTo(description);
        assertThat(wordSet.getWords()).isEmpty();
    }

    @Test
    public void getsAllWordSets() throws Exception {
        String name1 = "word-set-name1";
        String name2 = "word-set-name2";
        String description = "desc";
        WordSetMeta wordSetMeta1 = new WordSetMeta(userId.get(), name1, description);
        WordSetMeta wordSetMeta2 = new WordSetMeta(userId.get(), name2, description);

        long wsId1 = createWordSet(wordSetMeta1);
        long wsId2 = createWordSet(wordSetMeta2);

        mockMvc.perform(get("/word-sets/")
                                .param("userId", valueOf(userId.get())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(wsId1))
               .andExpect(jsonPath("$[0].name").value(name1))
               .andExpect(jsonPath("$[1].id").value(wsId2))
               .andExpect(jsonPath("$[1].name").value(name2));
    }

    @Test
    public void getsWordSetById() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);

        long wordSetId = createWordSet(wordSetMeta);

        performGetWordSetById(wordSetId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wordSetId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    public void createsWordByAddingToWordSet() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);

        String wordText = "word";
        String wordTranslation = "translation";
        String imageUrl = "img-url";
        WordMeta wordMeta = new WordMeta(wordText, wordTranslation, imageUrl);
        String speechUrl = "speech-url";
        when(ttsClient.getSpeech(wordText)).thenReturn(new SpeechResult(speechUrl));

        performAddWordToWordSet(wordSetId, wordMeta)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wordSetId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.words", hasSize(1)))
                .andExpect(jsonPath("$.words[0].text").value(wordText))
                .andExpect(jsonPath("$.words[0].translation").value(wordTranslation))
                .andExpect(jsonPath("$.words[0].image").value(imageUrl))
                .andExpect(jsonPath("$.words[0].sound").value(speechUrl))
                .andExpect(jsonPath("$.words[0].stage").value(NOT_LEARNED.name()));
    }

    @Test
    public void deletesWordSetWithAllCorrespondingWords() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);

        addWordToWordSet(wordSetId, new WordMeta("word1", "translation", "img-url"));
        WordSetResponse wordSetResponse = addWordToWordSet(wordSetId, new WordMeta("word2", "translation", "img-url"));

        mockMvc.perform(delete("/word-sets/{wordSetId}", wordSetId))
               .andExpect(status().isOk());

        performGetWordSetById(wordSetId)
                .andExpect(status().isNotFound());

        assertThat(wordSetResponse.getWords()).hasSize(2);

        wordSetResponse.getWords().stream()
                       .mapToLong(WordResponse::getId)
                       .mapToObj(wordRepository::findById)
                       .forEach(optionalWord -> assertThat(optionalWord).isEmpty());
    }

    @Test
    public void updatedWordSetMetaInformationWithoutTouchingWords() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);

        addWordToWordSet(wordSetId, new WordMeta("word1", "translation", "img-url"));
        addWordToWordSet(wordSetId, new WordMeta("word2", "translation", "img-url"));

        String updatedName = "updated-ws-name";
        String updatedDescription = "updated desc";
        WordSetMeta updatedMeta = new WordSetMeta(userId.get(), updatedName, updatedDescription);

        mockMvc.perform(patch("/word-sets/{wordSetId}", wordSetId)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(updatedMeta)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(wordSetId))
               .andExpect(jsonPath("$.name").value(updatedName))
               .andExpect(jsonPath("$.description").value(updatedDescription))
               .andExpect(jsonPath("$.words", hasSize(2)));
    }

    @Test
    public void deletesWordFromWordSet() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);

        addWordToWordSet(wordSetId, new WordMeta("word1", "translation", "img-url"));
        WordSetResponse wordSetResponse = addWordToWordSet(wordSetId, new WordMeta("word2", "translation", "img-url"));
        WordResponse wordToDelete = wordSetResponse.getWords().get(0);
        WordResponse wordThatShouldStay = wordSetResponse.getWords().get(1);

        mockMvc.perform(delete("/word-sets/{wordSetId}/words/{wordId}", wordSetId, wordToDelete.getId()))
               .andExpect(status().isOk());

        WordSetResponse response = getWordSetById(wordSetId);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getWords()).hasSize(1);
        assertThat(response.getWords()).containsOnly(wordThatShouldStay);
    }

}
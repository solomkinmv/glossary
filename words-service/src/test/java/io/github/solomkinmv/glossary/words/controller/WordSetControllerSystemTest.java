package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.repository.WordRepository;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.github.solomkinmv.glossary.words.persistence.domain.WordStage.NOT_LEARNED;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WordSetControllerSystemTest extends BaseTest {

    @Autowired
    private WordSetService wordSetService;

    @Autowired
    private WordRepository wordRepository;

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
        mockMvc.perform(get("/word-sets/")
                                .param("userId", valueOf(userId.get())))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[*].id", containsInAnyOrder((int) wordSet1.getId(),
                                                                 (int) wordSet2.getId())))
               .andExpect(jsonPath("$[*].name", containsInAnyOrder(wordSet1.getName(),
                                                                   wordSet2.getName())))
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("userId").description("User id to get all word sets")
                       ),
                       responseFields(
                               fieldWithPath("[]id").description("Word Set id"),
                               fieldWithPath("[]name").description("Word Set name"),
                               fieldWithPath("[]description").description("Word Set description"),
                               fieldWithPath("[]words[].id").description("Word id"),
                               fieldWithPath("[]words[].text").description("Word text"),
                               fieldWithPath("[]words[].translation").description("Word translation"),
                               fieldWithPath("[]words[].stage").description("Word learning stage"),
                               fieldWithPath("[]words[].image").description("Word image url"),
                               fieldWithPath("[]words[].sound").description("Word pronunciation sound url")
                       )
               ));
    }

    @Test
    public void getsWordSetById() throws Exception {
        String name = "word-set-name";
        String description = "desc";

        performGetWordSetById(wordSet1.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wordSet1.getId()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andDo(documentationHandler.document(
                        pathParameters(
                                parameterWithName("wordSetId").description("Word Set id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Word Set id"),
                                fieldWithPath("name").description("Word Set name"),
                                fieldWithPath("description").description("Word Set description"),
                                fieldWithPath("words[].id").description("Word id"),
                                fieldWithPath("words[].text").description("Word text"),
                                fieldWithPath("words[].translation").description("Word translation"),
                                fieldWithPath("words[].stage").description("Word learning stage"),
                                fieldWithPath("words[].image").description("Word image url"),
                                fieldWithPath("words[].sound").description("Word pronunciation sound url")
                        )
                ));
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
                .andExpect(jsonPath("$.words[0].stage").value(NOT_LEARNED.name()))
                .andDo(documentationHandler.document(
                        requestFields(
                                fieldWithPath("text").description("Word to add"),
                                fieldWithPath("translation").description("Word to add"),
                                fieldWithPath("image").description("New word's image url")
                        ),
                        pathParameters(
                                parameterWithName("wordSetId").description("Word Set id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Word Set id"),
                                fieldWithPath("name").description("Word Set name"),
                                fieldWithPath("description").description("Word Set description"),
                                fieldWithPath("words[].id").description("Word id"),
                                fieldWithPath("words[].text").description("Word text"),
                                fieldWithPath("words[].translation").description("Word translation"),
                                fieldWithPath("words[].stage").description("Word learning stage"),
                                fieldWithPath("words[].image").description("Word image url"),
                                fieldWithPath("words[].sound").description("Word pronunciation sound url")
                        )
                ));
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
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       pathParameters(
                               parameterWithName("wordSetId").description("Word Set id")
                       )
               ));

        verifyThatWordWithAllCorrespondingWordsHaveBeenDeleted(wordSetId, wordSetResponse);
    }

    private void verifyThatWordWithAllCorrespondingWordsHaveBeenDeleted(long wordSetId, WordSetResponse wordSetResponse) throws Exception {
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
               .andExpect(jsonPath("$.words", hasSize(2)))
               .andDo(documentationHandler.document(
                       requestFields(
                               fieldWithPath("userId").description("Appropriate user id"),
                               fieldWithPath("name").description("Word Set's name"),
                               fieldWithPath("description").description("Word Set's description")
                       ),
                       pathParameters(
                               parameterWithName("wordSetId").description("Word Set id")
                       ),
                       responseFields(
                               fieldWithPath("id").description("Word Set id"),
                               fieldWithPath("name").description("Word Set name"),
                               fieldWithPath("description").description("Word Set description"),
                               fieldWithPath("words[].id").description("Word id"),
                               fieldWithPath("words[].text").description("Word text"),
                               fieldWithPath("words[].translation").description("Word translation"),
                               fieldWithPath("words[].stage").description("Word learning stage"),
                               fieldWithPath("words[].image").description("Word image url"),
                               fieldWithPath("words[].sound").description("Word pronunciation sound url")
                       )
               ));
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
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       pathParameters(
                               parameterWithName("wordSetId").description("Word Set id"),
                               parameterWithName("wordId").description("Word id")
                       )
               ));

        verifyThatWordHasBeenDelitedFromWordSet(wordSetId, wordThatShouldStay);
    }

    private void verifyThatWordHasBeenDelitedFromWordSet(long wordSetId, WordResponse wordThatShouldStay) throws Exception {
        WordSetResponse response = getWordSetById(wordSetId);

        assertThat(response.getWords()).hasSize(1);
        assertThat(response.getWords()).containsOnly(wordThatShouldStay);
    }

}
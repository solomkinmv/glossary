package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.WithOAuthSubject;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import org.junit.Test;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WordControllerComponentTest extends BaseTest {

    @Test
    @WithOAuthSubject
    public void getsWordById() throws Exception {
        WordResponse word = wordSet1.getWords().stream()
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Test word set should contain at least one word"));

        mockMvc.perform(get("/words/{wordId}", word.getId())
                                .header(AUTHORIZATION, "Bearer foo"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(word.getId()))
               .andExpect(jsonPath("$.text").value(word.getText()))
               .andExpect(jsonPath("$.translation").value(word.getTranslation()))
               .andExpect(jsonPath("$.stage").value(word.getStage().toString()))
               .andExpect(jsonPath("$.image").value(word.getImage()))
               .andExpect(jsonPath("$.sound").value(word.getSound()))
               .andDo(documentationHandler.document(
                       requestHeaders(
                               headerWithName("Authorization").description("OAuth2 JWT token")),
                       pathParameters(
                               parameterWithName("wordId").description("Word id")
                       ),
                       responseFields(
                               fieldWithPath("id").description("Word id"),
                               fieldWithPath("text").description("Word text in original language"),
                               fieldWithPath("translation").description("Word translation"),
                               fieldWithPath("stage")
                                       .description("Word learning stage. One of: " + Arrays.toString(WordStage.values())),
                               fieldWithPath("image").description("URL to the associated word image"),
                               fieldWithPath("sound").description("URL to word pronunciation audio file")
                       )));
    }

    @Test
    @WithOAuthSubject
    public void updatesWord() throws Exception {
        WordResponse word = wordSet1.getWords().stream()
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Test word set should contain at least one word"));

        String newWordText = "abc";
        String newWordTranslation = "cba";
        String newWordImage = "new image url";
        WordMeta wordMeta = new WordMeta(newWordText, newWordTranslation, newWordImage);

        mockMvc.perform(patch("/words/{wordId}", word.getId())
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(wordMeta))
                                .header(AUTHORIZATION, "Bearer foo"))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$.id").value(word.getId()))
               .andExpect(jsonPath("$.text").value(newWordText))
               .andExpect(jsonPath("$.translation").value(newWordTranslation))
               .andExpect(jsonPath("$.image").value(newWordImage))
               .andDo(documentationHandler.document(
                       requestHeaders(
                               headerWithName("Authorization").description("OAuth2 JWT token")),
                       pathParameters(
                               parameterWithName("wordId").description("Word id")
                       ),
                       requestFields(
                               fieldWithPath("text").description("Updated word text"),
                               fieldWithPath("translation").description("Updated word translation"),
                               fieldWithPath("image").description("Updated word image url")
                       ),
                       responseFields(
                               fieldWithPath("id").description("Word id"),
                               fieldWithPath("text").description("Word text in original language"),
                               fieldWithPath("translation").description("Word translation"),
                               fieldWithPath("stage")
                                       .description("Word learning stage. One of: " + Arrays.toString(WordStage.values())),
                               fieldWithPath("image").description("URL to the associated word image"),
                               fieldWithPath("sound").description("URL to word pronunciation audio file")
                       )));
    }
}
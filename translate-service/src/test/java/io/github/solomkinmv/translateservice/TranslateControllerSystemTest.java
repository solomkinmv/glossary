package io.github.solomkinmv.translateservice;

import io.github.solomkinmv.translateservice.client.YandexTranslateClient;
import io.github.solomkinmv.translateservice.client.YandexTranslationResult;
import io.github.solomkinmv.translateservice.domain.Language;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TranslateControllerSystemTest {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private RestDocumentationResultHandler documentationHandler;
    @MockBean
    private YandexTranslateClient yandexTranslateClient;

    @Before
    public void setUp() {
        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();
    }

    @Test
    public void translatesTextWithoutSourceLanguage() throws Exception {
        String text = "hello world";
        String translation = "translation";
        String targetLanguage = "RUSSIAN";
        String sourceLanguage = "ENGLISH";
        YandexTranslationResult translationResult = new YandexTranslationResult((short) 200, "en-ru",
                                                                                singletonList(translation));
        when(yandexTranslateClient.translate(any(), eq(text), eq("ru")))
                .thenReturn(translationResult);

        mockMvc.perform(post("/translate")
                                .param("text", text)
                                .param("target", targetLanguage))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sourceText", equalTo(text)))
               .andExpect(jsonPath("$.result", hasSize(1)))
               .andExpect(jsonPath("$.result", contains(translation)))
               .andExpect(jsonPath("$.sourceLanguage", equalTo(sourceLanguage)))
               .andExpect(jsonPath("$.targetLanguage", equalTo(targetLanguage)))
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("text").description("Text to translate"),
                               parameterWithName("target")
                                       .description("Target language of translation. One of following languages: " +
                                                            Arrays.toString(Language.values()))
                       ), responseFields(
                               fieldWithPath("sourceText").description("Source text for translation"),
                               fieldWithPath("result").description("List of translated text results"),
                               fieldWithPath("sourceLanguage").description(
                                       "Source language. Absent if could not be autodetected"),
                               fieldWithPath("targetLanguage").description("Target language")
                       )
               ));
    }

    @Test
    public void translatesTextWithSourceLanguage() throws Exception {
        String text = "hello world";
        String translation = "translation";
        String targetLanguage = "RUSSIAN";
        String sourceLanguage = "ENGLISH";
        YandexTranslationResult translationResult = new YandexTranslationResult((short) 200, "en",
                                                                                singletonList(translation));
        when(yandexTranslateClient.translate(any(), eq(text), eq("en-ru")))
                .thenReturn(translationResult);

        mockMvc.perform(post("/translate")
                                .param("text", "hello world")
                                .param("source", sourceLanguage)
                                .param("target", targetLanguage))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sourceText", equalTo(text)))
               .andExpect(jsonPath("$.result", hasSize(1)))
               .andExpect(jsonPath("$.result", contains(translation)))
               .andExpect(jsonPath("$.sourceLanguage", equalTo(sourceLanguage)))
               .andExpect(jsonPath("$.targetLanguage", equalTo(targetLanguage)))
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("text").description("Text to translate"),
                               parameterWithName("source").description(
                                       "Optional source language of translation. One of following languages: " +
                                               Arrays.toString(Language.values())),
                               parameterWithName("target")
                                       .description("Target language of translation. One of following languages: " +
                                                            Arrays.toString(Language.values()))
                       ), responseFields(
                               fieldWithPath("sourceText").description("Source text for translation"),
                               fieldWithPath("result").description("List of translated text results"),
                               fieldWithPath("sourceLanguage").description(
                                       "Source language. Absent if could not be autodetected"),
                               fieldWithPath("targetLanguage").description("Target language")
                       )
               ));
    }
}

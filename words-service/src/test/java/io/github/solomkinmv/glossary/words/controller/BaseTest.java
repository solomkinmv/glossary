package io.github.solomkinmv.glossary.words.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");
    @MockBean
    public ResourceServerTokenServices resourceServerTokenServices;
    @MockBean
    protected TtsClient ttsClient;
    @Autowired
    protected ObjectMapper objectMapper;
    protected WordSetResponse wordSet1;
    protected MockMvc mockMvc;
    protected RestDocumentationResultHandler documentationHandler;
    protected WordSetResponse wordSet2;
    protected Map<String, WordResponse> wordsMap;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUpMockMvc() throws Exception {
        mockSecurity();

        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();

        when(ttsClient.getSpeech(anyString()))
                .thenReturn(new SpeechResult("speech-url"));

        wordSet1 = createWordSet();
        wordSet2 = createWordSet();

        wordsMap = Stream.concat(wordSet1.getWords().stream(), wordSet2.getWords().stream())
                         .collect(Collectors.toMap(WordResponse::getText, Function.identity()));
    }

    private void mockSecurity() {
        // Stub the remote call that loads the authentication object
        // SecurityContext should be already populated by WithOAuthSubjectSecurityContextFactory
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        when(resourceServerTokenServices.loadAuthentication(any()))
                .thenAnswer(invocation -> {
                    authentication.setDetails(details);
                    return authentication;
                });
    }

    long createWordSet(WordSetMeta wordSetMeta) throws Exception {
        String stringWordSetId = mockMvc.perform(post("/word-sets/")
                                                         .contentType(APPLICATION_JSON_UTF8)
                                                         .content(objectMapper.writeValueAsString(wordSetMeta))
                                                         .header(AUTHORIZATION, "Bearer foo"))
                                        .andExpect(status().isOk())
                                        .andDo(print())
                                        .andDo(documentationHandler.document(
                                                requestHeaders(
                                                        headerWithName("Authorization").description("OAuth2 JWT token")),
                                                requestFields(
                                                        fieldWithPath("name").description("Word Set's name"),
                                                        fieldWithPath("description").description(
                                                                "Word Set's description")
                                                ),
                                                responseBody(

                                                )
                                        ))
                                        .andReturn().getResponse().getContentAsString();

        return Long.parseLong(stringWordSetId);
    }

    ResultActions performAddWordToWordSet(long wordSetId, WordMeta wordMeta) throws Exception {
        return mockMvc.perform(post("/word-sets/{wordSetId}/words/", wordSetId)
                                       .contentType(APPLICATION_JSON_UTF8)
                                       .content(objectMapper.writeValueAsString(wordMeta))
                                       .header(AUTHORIZATION, "Bearer foo"));
    }

    @SneakyThrows
    WordSetResponse addWordToWordSet(long wordSetId, WordMeta wordMeta) {
        String contentAsString = performAddWordToWordSet(wordSetId, wordMeta)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, WordSetResponse.class);
    }

    ResultActions performGetWordSetById(long wordSetId) throws Exception {
        return mockMvc.perform(get("/word-sets/{wordSetId}", wordSetId)
                                       .header(AUTHORIZATION, "Bearer foo"));
    }

    WordSetResponse getWordSetById(long wordSetId) throws Exception {
        String contentAsString = performGetWordSetById(wordSetId)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, WordSetResponse.class);
    }

    private WordSetResponse createWordSet() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(name, description);
        long wordSetId = createWordSet(wordSetMeta);


        IntStream.rangeClosed(1, 9)
                 .mapToObj(i -> new WordMeta(wordSetId + "word" + i, wordSetId + "translation" + i, "img-url"))
                 .forEach(word -> addWordToWordSet(wordSetId, word));

        return getWordSetById(wordSetId);
    }
}

package io.github.solomkinmv.glossary.words.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {

    protected static final AtomicLong userId = new AtomicLong();

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");
    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;
    protected RestDocumentationResultHandler documentationHandler;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUpMockMvc() {
        userId.incrementAndGet();

        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();
    }

    long createWordSet(WordSetMeta wordSetMeta) throws Exception {
        String stringWordSetId = mockMvc.perform(post("/word-sets/")
                                                         .contentType(APPLICATION_JSON_UTF8)
                                                         .content(objectMapper.writeValueAsString(wordSetMeta)))
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return Long.parseLong(stringWordSetId);
    }

    ResultActions performAddWordToWordSet(long wordSetId, WordMeta wordMeta) throws Exception {
        return mockMvc.perform(post("/word-sets/{wordSetId}/words/", wordSetId)
                                       .contentType(APPLICATION_JSON_UTF8)
                                       .content(objectMapper.writeValueAsString(wordMeta)));
    }

    @SneakyThrows
    WordSetResponse addWordToWordSet(long wordSetId, WordMeta wordMeta) {
        String contentAsString = performAddWordToWordSet(wordSetId, wordMeta)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, WordSetResponse.class);
    }

    ResultActions performGetWordSetById(long wordSetId) throws Exception {
        return mockMvc.perform(get("/word-sets/{wordSetId}", wordSetId));
    }

    WordSetResponse getWordSetById(long wordSetId) throws Exception {
        String contentAsString = performGetWordSetById(wordSetId)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(contentAsString, WordSetResponse.class);
    }
}

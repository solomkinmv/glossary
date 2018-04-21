package io.github.solomkinmv.glossary.words.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.repository.WordSetRepository;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WordSetControllerSystemTest {

    private static final AtomicLong userId = new AtomicLong();

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");
    private RestDocumentationResultHandler documentationHandler;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private WordSetService wordSetService;
    @Autowired
    private WordSetRepository wordSetRepository;
    @Autowired
    private ObjectMapper objectMapper;

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
        assertThat(wordSet.getStudiedWords()).isEmpty();
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

        mockMvc.perform(get("/word-sets/{wordSetId}", wordSetId))
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

        WordMeta wordMeta = new WordMeta("word", "translation", "img-url");

        mockMvc.perform(post("/word-sets/{wordSetId}/words/", wordSetId)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(wordMeta)))
               .andExpect(status().isOk())
               .andDo(print());
    }

    private long createWordSet(WordSetMeta wordSetMeta) throws Exception {
        String stringWordSetId = mockMvc.perform(post("/word-sets/")
                                                         .contentType(APPLICATION_JSON_UTF8)
                                                         .content(objectMapper.writeValueAsString(wordSetMeta)))
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return Long.parseLong(stringWordSetId);
    }
}
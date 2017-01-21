package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.Application;
import io.github.solomkinmv.glossary.web.dto.IdDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link WordSetController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class StudiedWordSetControllerTest {

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private WordSetService wordSetService;
    @Autowired
    private WordService wordService;
    @Autowired
    private StudiedWordService studiedWordService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    public void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                                                    .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                                                    .findAny()
                                                    .orElse(null);

        assertNotNull("The JSON message converter must not be null");
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        wordService.save(new Word("word1", "translation1"));
        wordService.save(new Word("word2", "translation2"));
        wordService.save(new Word("word3", "translation3"));
        wordService.save(new Word("word4", "translation4"));

        wordSetService.save(new WordSet("wordSet1", "description1",
                Arrays.asList(
                        new StudiedWord(wordService.listAll().get(0)),
                        new StudiedWord(wordService.listAll().get(1)))));

        wordSetService.save(new WordSet("wordSet2", "description2",
                Arrays.asList(
                        new StudiedWord(wordService.listAll().get(2)),
                        new StudiedWord(wordService.listAll().get(3)))));
    }

    @After
    public void tearDown() throws Exception {
        wordSetService.deleteAll();
        studiedWordService.deleteAll();
    }

    @Test
    public void createWordSet() throws Exception {
        String wordSetJson = json(new WordSet(
                "wordSet3",
                "description3",
                Collections.emptyList()));

        mockMvc.perform(post("/api/wordSets")
                .contentType(contentType)
                .content(wordSetJson))
               .andExpect(status().isCreated());
    }

    @Test
    public void createExistingWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);

        String wordSetJson = json(wordSet);

        mockMvc.perform(post("/api/wordSets/" + wordSet.getId())
                .contentType(contentType)
                .content(wordSetJson))
               .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void getWordSets() throws Exception {
        List<WordSet> wordSetList = wordSetService.listAll();

        mockMvc.perform(get("/api/wordSets"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].wordSet.id", is(wordSetList.get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].wordSet.name", is(wordSetList.get(0).getName())))
               .andExpect(jsonPath("$.content[0].wordSet.description", is(wordSetList.get(0).getDescription())))
               .andExpect(jsonPath("$.content[0].wordSet.studiedWords[0].id",
                       is(wordSetList.get(0).getStudiedWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].wordSet.studiedWords[1].id",
                       is(wordSetList.get(0).getStudiedWords().get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].wordSet.id", is(wordSetList.get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].wordSet.name", is(wordSetList.get(1).getName())))
               .andExpect(jsonPath("$.content[1].wordSet.description", is(wordSetList.get(1).getDescription())))
               .andExpect(jsonPath("$.content[1].wordSet.studiedWords[0].id",
                       is(wordSetList.get(1).getStudiedWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[1].wordSet.studiedWords[1].id",
                       is(wordSetList.get(1).getStudiedWords().get(1).getId().intValue())));
    }

    @Test
    public void getWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);

        mockMvc.perform(get("/api/wordSets/" + wordSet.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.wordSet.id", is(wordSet.getId().intValue())))
               .andExpect(jsonPath("$.wordSet.name", is(wordSet.getName())))
               .andExpect(jsonPath("$.wordSet.description", is(wordSet.getDescription())))
               .andExpect(jsonPath("$.wordSet.studiedWords[0].id",
                       is(wordSet.getStudiedWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.wordSet.studiedWords[1].id",
                       is(wordSet.getStudiedWords().get(1).getId().intValue())));
    }

    @Test
    public void getAbsentWordSet() throws Exception {
        mockMvc.perform(get("/api/wordSets/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void deleteWordSets() throws Exception {
        mockMvc.perform(delete("/api/wordSets"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);
        mockMvc.perform(delete("/api/wordSets/" + wordSet.getId()))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteAbsentWordSet() throws Exception {
        mockMvc.perform(delete("/api/wordSets/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void putWordSets() throws Exception {
        mockMvc.perform(put("/api/wordSets"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void putWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);

        wordSet.setDescription("description 42");

        String wordSetJson = json(wordSet);

        mockMvc.perform(put("/api/wordSets/" + wordSet.getId())
                .contentType(contentType)
                .content(wordSetJson))
               .andExpect(status().isOk());

        WordSet updatedWordSet = wordSetService.getById(wordSet.getId()).orElse(null);

        assertEquals(wordSet, updatedWordSet);
    }

    @Test
    public void putAbsentWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);
        String wordSetJson = json(wordSet);

        mockMvc.perform(put("/api/wordSets/0")
                .contentType(contentType)
                .content(wordSetJson))
               .andExpect(status().isNotFound());
    }

    @Test
    public void getWordSetStudiedWords() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);
        List<StudiedWord> wordSetStudiedWords = wordSet.getStudiedWords();
        mockMvc.perform(get("/api/wordSets/" + wordSet.getId() + "/words"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(wordSetStudiedWords.size())))
               .andExpect(jsonPath("$.content[0].studiedWord.id", is(wordSetStudiedWords.get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].studiedWord.word.text",
                       is(wordSetStudiedWords.get(0).getWord().getText())))
               .andExpect(jsonPath("$.content[0].studiedWord.word.translation",
                       is(wordSetStudiedWords.get(0).getWord().getTranslation())))
               .andExpect(jsonPath("$.content[1].studiedWord.id", is(wordSetStudiedWords.get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].studiedWord.word.text",
                       is(wordSetStudiedWords.get(1).getWord().getText())))
               .andExpect(jsonPath("$.content[1].studiedWord.word.translation",
                       is(wordSetStudiedWords.get(1).getWord().getTranslation())));
    }

    @Test
    public void addWordToWordSet() throws Exception {
        List<WordSet> wordSetList = wordSetService.listAll();
        WordSet wordSet = wordSetList.get(0);
        StudiedWord studiedWordToAdd = wordSetList.get(1).getStudiedWords().get(0);

        IdDto idDto = new IdDto(studiedWordToAdd.getId());
        String idJson = json(idDto);

        mockMvc.perform(post("/api/wordSets/" + wordSet.getId() + "/words")
                .contentType(contentType)
                .content(idJson))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteWordFromWordSet() throws Exception {
        WordSet wordSet = wordSetService.listAll().get(0);
        StudiedWord studiedWord = wordSet.getStudiedWords().get(0);

        mockMvc.perform(delete("/api/wordSets/" + wordSet.getId() + "/words/" + studiedWord.getId()))
               .andExpect(status().isOk());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }
}
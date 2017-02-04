package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.Application;
import org.apache.http.client.utils.URIBuilder;
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
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link WordController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class WordControllerTest {

    private final List<Word> wordList = new ArrayList<>();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                        MediaType.APPLICATION_JSON.getSubtype(),
                                                        StandardCharsets.UTF_8);
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private WordService wordService;
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

        wordService.deleteAll();

        wordList.add(wordService.save(new Word("word1", "translation1")));
        wordList.add(wordService.save(new Word("word2", "translation2")));
    }

    @Test
    public void searchWords() throws Exception {
        URI uri = new URIBuilder("/api/words/search")
                .addParameter("query", "word")
                .build();

        mockMvc.perform(get(uri))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void searchSpecificWord() throws Exception {
        String wordText = "word1";
        URI uri = new URIBuilder("/api/words/search")
                .addParameter("query", wordText)
                .build();

        mockMvc.perform(get(uri))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].word.text", is(wordText)));
    }

    @Test
    public void createWord() throws Exception {
        String wordJson = json(new Word("text", "translation"));

        mockMvc.perform(post("/api/words")
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isCreated());
    }

    private String json(Word o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }

    @Test
    public void createExistingWord() throws Exception {
        Word word = wordService.listAll().get(0);

        String wordJson = json(word);

        mockMvc.perform(post("/api/words/" + word.getId())
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void getWords() throws Exception {
        mockMvc.perform(get("/api/words"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].word.id", is(wordList.get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].word.text", is(wordList.get(0).getText())))
               .andExpect(jsonPath("$.content[0].word.translation", is(wordList.get(0).getTranslation())))
               .andExpect(jsonPath("$.content[1].word.id", is(wordList.get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].word.text", is(wordList.get(1).getText())))
               .andExpect(jsonPath("$.content[1].word.translation", is(wordList.get(1).getTranslation())));
    }

    @Test
    public void getWord() throws Exception {
        Word word = wordList.get(0);

        mockMvc.perform(get("/api/words/" + word.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.word.id", is(word.getId().intValue())))
               .andExpect(jsonPath("$.word.text", is(word.getText())))
               .andExpect(jsonPath("$.word.translation", is(word.getTranslation())));
    }

    @Test
    public void getAbsentWord() throws Exception {
        mockMvc.perform(get("/api/words/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void deleteWords() throws Exception {
        mockMvc.perform(delete("/api/words/"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteWord() throws Exception {
        Word word = wordList.get(0);

        mockMvc.perform(delete("/api/words/" + word.getId()))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteAbsentWord() throws Exception {
        mockMvc.perform(delete("/api/words/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void putWords() throws Exception {
        mockMvc.perform(put("/api/words"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void putWord() throws Exception {
        Word word = wordList.get(0);

        word.setTranslation("translation 2");

        String wordJson = json(word);

        mockMvc.perform(put("/api/words/" + word.getId())
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isOk());

        Word updatedWord = wordService.getById(word.getId()).orElse(null);

        assertEquals(word, updatedWord);
    }

    @Test
    public void putAbsentWord() throws Exception {
        Word word = wordList.get(0);
        String wordJson = json(word);

        mockMvc.perform(put("/api/words/0")
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isNotFound());
    }
}
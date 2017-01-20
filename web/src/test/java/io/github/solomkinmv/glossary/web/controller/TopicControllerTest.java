package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import io.github.solomkinmv.glossary.service.domain.WordService;
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
import java.util.ArrayList;
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
 * Test for {@link TopicController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TopicControllerTest {

    private final List<Topic> topicList = new ArrayList<>();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private TopicService topicService;
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

        topicList.add(topicService.save(
                new Topic("topic1",
                        "description1",
                        Arrays.asList(
                                new Word("word1", "translation1"),
                                new Word("word2", "translation2")))));

        topicList.add(topicService.save(
                new Topic("topic2",
                        "description2",
                        Arrays.asList(
                                new Word("word3", "translation3"),
                                new Word("word4", "translation4")))));
    }

    @After
    public void tearDown() throws Exception {
        topicService.deleteAll();
        wordService.deleteAll();
    }

    @Test
    public void createTopic() throws Exception {
        String topicJson = json(new Topic(
                "topic3",
                "description3",
                Collections.emptyList()));

        mockMvc.perform(post("/api/topics")
                .contentType(contentType)
                .content(topicJson))
               .andExpect(status().isCreated());
    }

    @Test
    public void createExistingTopic() throws Exception {
        Topic topic = topicService.listAll().get(0);

        String topicJson = json(topic);

        mockMvc.perform(post("/api/topics/" + topic.getId())
                .contentType(contentType)
                .content(topicJson))
               .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void getTopics() throws Exception {
        mockMvc.perform(get("/api/topics"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].topic.id", is(topicList.get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].topic.name", is(topicList.get(0).getName())))
               .andExpect(jsonPath("$.content[0].topic.description", is(topicList.get(0).getDescription())))
               .andExpect(jsonPath("$.content[0].topic.words[0].id",
                       is(topicList.get(0).getWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].topic.words[1].id",
                       is(topicList.get(0).getWords().get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].topic.id", is(topicList.get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].topic.name", is(topicList.get(1).getName())))
               .andExpect(jsonPath("$.content[1].topic.description", is(topicList.get(1).getDescription())))
               .andExpect(jsonPath("$.content[1].topic.words[0].id",
                       is(topicList.get(1).getWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[1].topic.words[1].id",
                       is(topicList.get(1).getWords().get(1).getId().intValue())));
    }

    @Test
    public void getTopic() throws Exception {
        Topic topic = topicList.get(0);

        mockMvc.perform(get("/api/topics/" + topic.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.topic.id", is(topic.getId().intValue())))
               .andExpect(jsonPath("$.topic.name", is(topic.getName())))
               .andExpect(jsonPath("$.topic.description", is(topic.getDescription())))
               .andExpect(jsonPath("$.topic.words[0].id", is(topic.getWords().get(0).getId().intValue())))
               .andExpect(jsonPath("$.topic.words[1].id", is(topic.getWords().get(1).getId().intValue())));
    }

    @Test
    public void getAbsentTopic() throws Exception {
        mockMvc.perform(get("/api/topics/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTopics() throws Exception {
        mockMvc.perform(delete("/api/topics"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteTopic() throws Exception {
        Topic topic = topicList.get(0);
        mockMvc.perform(delete("/api/topics/" + topic.getId()))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteAbsentTopic() throws Exception {
        mockMvc.perform(delete("/api/topics/0"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void putTopics() throws Exception {
        mockMvc.perform(put("/api/topics"))
               .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void putTopic() throws Exception {
        Topic topic = topicList.get(0);

        topic.setDescription("description 42");

        String topicJson = json(topic);

        mockMvc.perform(put("/api/topics/" + topic.getId())
                .contentType(contentType)
                .content(topicJson))
               .andExpect(status().isOk());

        Topic updatedTopic = topicService.getById(topic.getId()).orElse(null);

        assertEquals(topic, updatedTopic);
    }

    @Test
    public void putAbsentTopic() throws Exception {
        Topic topic = topicList.get(0);
        String topicJson = json(topic);

        mockMvc.perform(put("/api/topics/0")
                .contentType(contentType)
                .content(topicJson))
               .andExpect(status().isNotFound());
    }

    @Test
    public void getTopicWords() throws Exception {
        Topic topic = topicList.get(0);
        List<Word> topicWords = topic.getWords();
        mockMvc.perform(get("/api/topics/" + topic.getId() + "/words"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.content", hasSize(topicWords.size())))
               .andExpect(jsonPath("$.content[0].word.id", is(topicWords.get(0).getId().intValue())))
               .andExpect(jsonPath("$.content[0].word.text", is(topicWords.get(0).getText())))
               .andExpect(jsonPath("$.content[0].word.translation", is(topicWords.get(0).getTranslation())))
               .andExpect(jsonPath("$.content[1].word.id", is(topicWords.get(1).getId().intValue())))
               .andExpect(jsonPath("$.content[1].word.text", is(topicWords.get(1).getText())))
               .andExpect(jsonPath("$.content[1].word.translation", is(topicWords.get(1).getTranslation())));
    }

    @Test
    public void addWordToTopic() throws Exception {
        Topic topic = topicList.get(0);
        Word wordToAdd = topicList.get(1).getWords().get(0);

        IdDto idDto = new IdDto(wordToAdd.getId());
        String idJson = json(idDto);

        mockMvc.perform(post("/api/topics/" + topic.getId() + "/words")
                .contentType(contentType)
                .content(idJson))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteWordFromTopic() throws Exception {
        Topic topic = topicList.get(0);
        Word word = topic.getWords().get(0);

        mockMvc.perform(delete("/api/topics/" + topic.getId() + "/words/" + word.getId()))
               .andExpect(status().isOk());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }
}
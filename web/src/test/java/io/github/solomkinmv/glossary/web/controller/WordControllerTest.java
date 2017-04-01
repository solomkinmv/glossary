package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for {@link WordController}
 */
@Slf4j
public class WordControllerTest extends MockMvcBase {

    private final List<StudiedWord> wordList = new ArrayList<>();
    @Autowired
    private WordService wordService;
    @Autowired
    private UserDictionaryService userDictionaryService;
    private AuthenticatedUser authenticatedUser;

    @Before
    public void setUp() throws Exception {
        log.info("Setting up test method");
        wordService.deleteAll();

        StudiedWord word1 = new StudiedWord("word1", "translation1");
        StudiedWord word2 = new StudiedWord("word2", "translation2");
        StudiedWord word3 = new StudiedWord("word3", "translation3");
        StudiedWord word4 = new StudiedWord("word4", "translation4");
        StudiedWord word5 = new StudiedWord("word5", "translation5");
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        wordList.add(word5);

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        WordSet ws1 = new WordSet("ws1", "desc", Arrays.asList(word1, word2));
        WordSet ws2 = new WordSet("ws2", "desc", Arrays.asList(word3, word4));
        WordSet ws3 = new WordSet("ws3", "desc", Collections.singletonList(word5));

        Set<WordSet> userOneSet = new HashSet<>();
        userOneSet.add(ws1);
        userOneSet.add(ws2);
        Set<WordSet> userTwoSet = new HashSet<>();
        userTwoSet.add(ws3);

        UserDictionary dict1 = new UserDictionary(userOneSet, user1);
        UserDictionary dict2 = new UserDictionary(userTwoSet, user2);

        userDictionaryService.save(dict1);
        userDictionaryService.save(dict2);

        authenticatedUser = new AuthenticatedUser("user1",
                                                  Collections.singletonList(
                                                          new SimpleGrantedAuthority(RoleType.USER.authority())));
    }

    @Test
    public void getAllWords() throws Exception {
        mockMvc.perform(get("/api/words").with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$._embedded.wordResourceList", hasSize(4)))
               .andExpect(jsonPath("$._embedded.wordResourceList[0].word.id", is(wordList.get(0).getId().intValue())))
               .andExpect(jsonPath("$._embedded.wordResourceList[0].word.text", is(wordList.get(0).getText())))
               .andExpect(jsonPath("$._embedded.wordResourceList[0].word.translation",
                                   is(wordList.get(0).getTranslation())))
               .andExpect(jsonPath("$._embedded.wordResourceList[1].word.id", is(wordList.get(1).getId().intValue())))
               .andExpect(jsonPath("$._embedded.wordResourceList[1].word.text", is(wordList.get(1).getText())))
               .andExpect(jsonPath("$._embedded.wordResourceList[1].word.translation",
                                   is(wordList.get(1).getTranslation())))
               .andExpect(jsonPath("$._embedded.wordResourceList[2].word.id", is(wordList.get(2).getId().intValue())))
               .andExpect(jsonPath("$._embedded.wordResourceList[2].word.text", is(wordList.get(2).getText())))
               .andExpect(jsonPath("$._embedded.wordResourceList[2].word.translation",
                                   is(wordList.get(2).getTranslation())))
               .andExpect(jsonPath("$._embedded.wordResourceList[3].word.id", is(wordList.get(3).getId().intValue())))
               .andExpect(jsonPath("$._embedded.wordResourceList[3].word.text", is(wordList.get(3).getText())))
               .andExpect(jsonPath("$._embedded.wordResourceList[3].word.translation",
                                   is(wordList.get(3).getTranslation())))
        ;
    }

    @Test
    public void getWordById() throws Exception {
        StudiedWord word = wordList.get(0);

        mockMvc.perform(get("/api/words/" + word.getId()).with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.word.id", is(word.getId().intValue())))
               .andExpect(jsonPath("$.word.text", is(word.getText())))
               .andExpect(jsonPath("$.word.translation", is(word.getTranslation())));
    }

    @Test
    public void deleteWord() throws Exception {
        StudiedWord word = wordList.get(0);

        mockMvc.perform(delete("/api/words/" + word.getId()).with(userToken()))
               .andExpect(status().isOk());

        List<StudiedWord> studiedWords = wordService.listByUsername(authenticatedUser.getUsername());
        assertEquals(3, studiedWords.size());
    }

    protected AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    /*@Test
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
        String wordJson = jsonConverter.toJson(new StudiedWord("text", "translation"));

        mockMvc.perform(post("/api/words")
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isCreated());
    }

    @Test
    public void createExistingWord() throws Exception {
        StudiedWord word = wordService.listAll().get(0);

        String wordJson = jsonConverter.toJson(word);

        mockMvc.perform(post("/api/words/" + word.getId())
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().is(HttpStatus.CONFLICT.value()));
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
        StudiedWord word = wordList.get(0);

        word.setTranslation("translation 2");

        String wordJson = jsonConverter.toJson(word);

        mockMvc.perform(put("/api/words/" + word.getId())
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isOk());

        StudiedWord updatedWord = wordService.getById(word.getId()).orElse(null);

        assertEquals(word, updatedWord);
    }

    @Test
    public void putAbsentWord() throws Exception {
        StudiedWord word = wordList.get(0);
        String wordJson = jsonConverter.toJson(word);

        mockMvc.perform(put("/api/words/0")
                                .contentType(contentType)
                                .content(wordJson))
               .andExpect(status().isNotFound());
    }*/
}
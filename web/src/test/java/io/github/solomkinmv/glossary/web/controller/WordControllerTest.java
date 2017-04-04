package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.transaction.Transactional;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for {@link WordController}
 */
@Slf4j
@Transactional
public class WordControllerTest extends MockMvcBase {

    private final List<StudiedWord> wordList = new ArrayList<>();
    @Autowired
    private WordService wordService;
    @Autowired
    private UserDictionaryService userDictionaryService;
    @Autowired
    private WordDao wordDao;
    private AuthenticatedUser authenticatedUser;

    @Before
    public void setUp() throws Exception {
        log.info("Setting up test method");

        StudiedWord word1 = new StudiedWord("word1", "translation1");
        StudiedWord word2 = new StudiedWord("word2", "translation2");
        StudiedWord word3 = new StudiedWord("word3", "translation3");
        StudiedWord word4 = new StudiedWord("word4", "translation4");
        StudiedWord word5 = new StudiedWord("word5", "translation5");
        StudiedWord word6 = new StudiedWord("word5", "translation55");
        wordList.add(wordService.save(word1));
        wordList.add(wordService.save(word2));
        wordList.add(wordService.save(word3));
        wordList.add(wordService.save(word4));
        wordList.add(wordService.save(word5));
        wordList.add(wordService.save(word6));

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        WordSet ws1 = new WordSet("ws1", "desc", Arrays.asList(word1, word2));
        WordSet ws2 = new WordSet("ws2", "desc", Arrays.asList(word3, word4, word5));
        WordSet ws3 = new WordSet("ws3", "desc", Collections.singletonList(word6));

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
        Integer[] integers = wordList.stream().map(word -> word.getId().intValue()).limit(4).toArray(Integer[]::new);
        Matcher<Iterable<? extends Integer>> idMatcher = containsInAnyOrder(integers);

        mockMvc.perform(get("/api/words").with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$._embedded.wordResourceList", hasSize(4)))
               .andExpect(jsonPath("$._embedded.wordResourceList[*].word.id", idMatcher))
               .andExpect(jsonPath("$._embedded.wordResourceList[*].word.text", not(contains(nullValue()))))
               .andExpect(jsonPath("$._embedded.wordResourceList[*].word.translation", not(contains(nullValue()))));
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
    public void getOtherUsersWordById() throws Exception {
        StudiedWord word = wordList.get(wordList.size() - 1);

        mockMvc.perform(get("/api/words/" + word.getId()).with(userToken()))
               .andExpect(status().isNotFound());
    }

    @Test
    public void searchForPresentWords() throws Exception {
        mockMvc.perform(get("/api/words/search")
                                .param("text", "word")
                                .with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.result.records", hasSize(5)))
               .andExpect(jsonPath("$.result.records[0].text", is(wordList.get(0).getText())))
               .andExpect(jsonPath("$.result.records[0].translations", hasSize(1)))
               .andExpect(jsonPath("$.result.records[0].translations[0]", is(wordList.get(0).getTranslation())))
               .andExpect(jsonPath("$.result.records[4].text", is(wordList.get(4).getText())))
               .andExpect(jsonPath("$.result.records[4].translations", hasSize(2)))
               .andExpect(jsonPath("$.result.records[4].translations",
                                   containsInAnyOrder(wordList.get(4).getTranslation(),
                                                      wordList.get(5).getTranslation())));

    }

    /*@Test
    public void createWord() throws Exception {
        StudiedWord word = new StudiedWord("word1", "translation11", WordStage.LEARNING);
        String json = jsonConverter.toJson(word);

        mockMvc.perform(post("/api/words")
                                .contentType(contentType)
                                .content(json)
                                .with(userToken()))
               .andExpect(status().isCreated());

        assertEquals(5, wordService.listByUsername(authenticatedUser.getUsername()).size());
        System.out.println();
    }

    @Test
    public void deleteWord() throws Exception {
        StudiedWord word = wordList.get(0);

        mockMvc.perform(delete("/api/words/" + word.getId()).with(userToken()))
               .andExpect(status().isOk());

        List<StudiedWord> studiedWords = wordService.listByUsername(authenticatedUser.getUsername());
        assertEquals(3, studiedWords.size());
    }*/

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
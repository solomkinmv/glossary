package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import io.github.solomkinmv.glossary.web.dto.WordDto;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import io.github.solomkinmv.glossary.web.dto.WordSetMetaDto;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test for {@link WordSetController}
 */
@Transactional
public class WordSetControllerTest extends MockMvcBase {

    private final List<StudiedWord> wordList = new ArrayList<>();
    private final List<WordSet> wordSets = new ArrayList<>();
    private int[] wordIds;
    private int[] wordSetIds;
    @Autowired
    private UserDictionaryService userDictionaryService;
    @Autowired
    private WordService wordService;
    @Autowired
    private WordSetService wordSetService;
    private AuthenticatedUser authenticatedUser;

    @Before
    public void setUp() throws Exception {
        StudiedWord word1 = new StudiedWord("word1", "translation1");
        StudiedWord word2 = new StudiedWord("word2", "translation2");
        StudiedWord word3 = new StudiedWord("word3", "translation3");
        StudiedWord word4 = new StudiedWord("word4", "translation4");
        StudiedWord word5 = new StudiedWord("word5", "translation5");
        wordList.add(wordService.save(word1));
        wordList.add(wordService.save(word2));
        wordList.add(wordService.save(word3));
        wordList.add(wordService.save(word4));
        wordList.add(wordService.save(word5));

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        WordSet ws1 = wordSetService.save(
                new WordSet("ws1", "desc", new ArrayList<>(Arrays.asList(word1, word2))));
        WordSet ws2 = wordSetService.save(
                new WordSet("ws2", "desc", new ArrayList<>(Arrays.asList(word3, word4))));
        WordSet ws3 = wordSetService.save(
                new WordSet("ws3", "desc", new ArrayList<>(Collections.singletonList(word5))));
        wordSets.add(ws1);
        wordSets.add(ws2);
        wordSets.add(ws3);

        Set<WordSet> userOneSet = new HashSet<>();
        userOneSet.add(ws1);
        userOneSet.add(ws2);
        Set<WordSet> userTwoSet = new HashSet<>();
        userTwoSet.add(ws3);

        UserDictionary dict1 = new UserDictionary(userOneSet, user1);
        UserDictionary dict2 = new UserDictionary(userTwoSet, user2);

        userDictionaryService.save(dict1);
        userDictionaryService.save(dict2);

        wordIds = wordList.stream().mapToInt(studiedWord -> studiedWord.getId().intValue()).toArray();
        wordSetIds = wordSets.stream().mapToInt(wordSet -> wordSet.getId().intValue()).toArray();
        authenticatedUser = new AuthenticatedUser("user1",
                                                  Collections.singletonList(
                                                          new SimpleGrantedAuthority(RoleType.USER.authority())));
    }

    @Test
    public void getAllWordSets() throws Exception {
        mockMvc.perform(get("/api/sets").with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$._embedded.wordSetResourceList", hasSize(2)))
               .andExpect(jsonPath("$._embedded.wordSetResourceList[*].set.id",
                                   containsInAnyOrder(wordSetIds[0], wordSetIds[1])))
               .andExpect(jsonPath("$..set[?(@.id==" + wordSetIds[0] + ")].name",
                                   contains(wordSets.get(0).getName())))
               .andExpect(jsonPath("$..set[?(@.id==" + wordSetIds[0] + ")].description",
                                   contains(wordSets.get(0).getDescription())))
               .andExpect(jsonPath("$..set[?(@.id==" + wordSetIds[0] + ")].words[*].id",
                                   containsInAnyOrder(wordIds[0], wordIds[1])))
               .andExpect(jsonPath("$..set[?(@.id==" + wordSetIds[1] + ")].words[*].id",
                                   containsInAnyOrder(wordIds[2], wordIds[3])));
    }

    @Test
    public void getWordSetById() throws Exception {
        WordSet wordSet = wordSets.get(0);
        mockMvc.perform(get("/api/sets/{wordSetId}", wordSetIds[0])
                                .with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.set.id", is(wordSetIds[0])))
               .andExpect(jsonPath("$.set.name", is(wordSet.getName())))
               .andExpect(jsonPath("$.set.description", is(wordSet.getDescription())))
               .andExpect(jsonPath("$.set.words[*].id", containsInAnyOrder(wordIds[0], wordIds[1])));
    }

    @Test
    public void createEmptyWordSet() throws Exception {
        String name = "createdWs";
        String description = "some desc";
        WordSetDto wordSetDto = new WordSetDto(null, name, description, null);

        MvcResult mvcResult = mockMvc.perform(post("/api/sets")
                                                      .with(userToken())
                                                      .contentType(contentType)
                                                      .content(jsonConverter.toJson(wordSetDto)))
                                     .andExpect(status().isCreated())
                                     .andExpect(header().string("Location", is(notNullValue())))
                                     .andReturn();

        long id = extractIdFromLocationHeader(mvcResult);
        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername(id, authenticatedUser.getUsername());

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();

        assertEquals(id, wordSet.getId().longValue());
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
        assertThat(wordSet.getStudiedWords(), emptyCollectionOf(StudiedWord.class));
    }

    private long extractIdFromLocationHeader(MvcResult mvcResult) {
        String location = mvcResult.getResponse().getHeader("Location");
        String[] urlChunks = location.split("/");
        return Long.parseLong(urlChunks[urlChunks.length - 1]);
    }

    @Test
    public void createWordSetWithWords() throws Exception {
        String name = "createdWs";
        String description = "some desc";
        WordSetDto wordSetDto = new WordSetDto(null, name, description, new ArrayList<>(Arrays.asList(
                new WordDto(null, "book1", "книга1", WordStage.LEARNING, "img1", "sound1"),
                new WordDto(null, "book2", "книга2", WordStage.LEARNING, "img2", "sound2")
        )));

        MvcResult mvcResult = mockMvc.perform(post("/api/sets")
                                                      .with(userToken())
                                                      .contentType(contentType)
                                                      .content(jsonConverter.toJson(wordSetDto)))
                                     .andExpect(status().isCreated())
                                     .andExpect(header().string("Location", is(notNullValue())))
                                     .andReturn();

        long id = extractIdFromLocationHeader(mvcResult);
        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername(id, authenticatedUser.getUsername());

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();

        assertEquals(id, wordSet.getId().longValue());
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
        List<StudiedWord> studiedWords = wordSet.getStudiedWords();
        assertThat(studiedWords, hasSize(2));
    }

    @Test
    public void deleteWordSet() throws Exception {
        mockMvc.perform(delete("/api/sets/{wordSetId}", wordSetIds[0])
                                .with(userToken()))
               .andExpect(status().isOk());

        Optional<UserDictionary> dictionaryOptional = userDictionaryService.getByUsername(
                authenticatedUser.getUsername());

        assertTrue(dictionaryOptional.isPresent());
        UserDictionary userDictionary = dictionaryOptional.get();

        assertFalse(userDictionary.getWordSets().stream().noneMatch(wordSet -> wordSetIds[0] == wordSet.getId()));
    }

    @Test
    public void deleteWordFromWordSet() throws Exception {
        mockMvc.perform(delete("/api/sets/{wordSetId}/words/{wordId}", wordSetIds[0], wordIds[0])
                                .with(userToken()))
               .andExpect(status().isOk());

        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername((long) wordSetIds[0],
                                                                              authenticatedUser.getUsername());

        assertTrue(wordSetOptional.isPresent());
        WordSet wordSet = wordSetOptional.get();

        assertTrue(wordSet.getStudiedWords().stream().noneMatch(word -> word.getId() == wordIds[0]));

        Optional<StudiedWord> wordOptional = wordService.getById((long) wordIds[0]);
        assertFalse(wordOptional.isPresent());
    }

    @Test
    public void addWordToWordSet() throws Exception {
        WordDto wordDto = new WordDto(null, "book1", "книга1", WordStage.LEARNING, "img1", null);

        MvcResult mvcResult = mockMvc.perform(post("/api/sets/{wordSetId}/words", wordSetIds[0])
                                                      .with(userToken())
                                                      .contentType(contentType)
                                                      .content(jsonConverter.toJson(wordDto)))
                                     .andExpect(status().isCreated())
                                     .andExpect(header().string("Location", is(notNullValue())))
                                     .andReturn();

        long id = extractIdFromLocationHeader(mvcResult);
        Optional<StudiedWord> studiedWordOptional = wordSetService
                .getByIdAndUsername((long) wordSetIds[0], authenticatedUser.getUsername())
                .map(WordSet::getStudiedWords)
                .flatMap(words -> words.stream()
                                       .filter(word -> word.getId() == id)
                                       .findAny());

        assertTrue(studiedWordOptional.isPresent());

        StudiedWord studiedWord = studiedWordOptional.get();
        assertEquals(studiedWord.getId(), (Long) id);
        assertEquals("book1", studiedWord.getText());
        assertEquals("книга1", studiedWord.getTranslation());
        assertNotNull(studiedWord.getSound());
    }

    @Test
    public void updateWordSetMetaInformation() throws Exception {
        String name = "updated name";
        String description = "updated description";
        WordSetMetaDto metaDto = new WordSetMetaDto(name, description);

        mockMvc.perform(patch("/api/sets/{wordSetId}", wordSetIds[0])
                                .with(userToken())
                                .contentType(contentType)
                                .content(jsonConverter.toJson(metaDto)))
               .andExpect(status().isOk());

        Optional<WordSet> wordSetOptional = wordSetService.getById((long) wordSetIds[0]);

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
    }

    @Override
    protected AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

/*
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
    public void getSet() throws Exception {
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
    }*/
}
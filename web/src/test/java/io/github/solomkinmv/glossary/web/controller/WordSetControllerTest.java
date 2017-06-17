package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import io.github.solomkinmv.glossary.web.dto.WordDto;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import io.github.solomkinmv.glossary.web.dto.WordSetMetaDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @Before
    public void setUp() throws Exception {
        StudiedWord word1 = new StudiedWord("word1", "слово1");
        StudiedWord word2 = new StudiedWord("word2", "слово2");
        StudiedWord word3 = new StudiedWord("word3", "слово3");
        StudiedWord word4 = new StudiedWord("word4", "слово4");
        StudiedWord word5 = new StudiedWord("word5", "слово5");
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
                                   containsInAnyOrder(wordIds[2], wordIds[3])))
               .andDo(documentationHandler.document(
                       responseFields(
                               fieldWithPath("_embedded.wordSetResourceList[].set.id").description("Word set's id"),
                               fieldWithPath("_embedded.wordSetResourceList[].set.name").description("Word set's name"),
                               fieldWithPath("_embedded.wordSetResourceList[].set.description")
                                       .description("Word set's description"),
                               fieldWithPath("_embedded.wordSetResourceList[].set.words[]")
                                       .description("Array of words"),
                               fieldWithPath("_embedded.wordSetResourceList[]._links").ignored()
                       ), headersSnippet
               ));
    }

    @Test
    public void getWordSetById() throws Exception {
        WordSet wordSet = wordSets.get(0);
        mockMvc.perform(get("/api/sets/{id}", wordSetIds[0])
                                .with(userToken()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(contentType))
               .andExpect(jsonPath("$.set.id", is(wordSetIds[0])))
               .andExpect(jsonPath("$.set.name", is(wordSet.getName())))
               .andExpect(jsonPath("$.set.description", is(wordSet.getDescription())))
               .andExpect(jsonPath("$.set.words[*].id", containsInAnyOrder(wordIds[0], wordIds[1])))
               .andDo(documentationHandler.document(
                       responseFields(
                               fieldWithPath("set.id").description("Word set's id"),
                               fieldWithPath("set.name").description("Word set's name"),
                               fieldWithPath("set.description").description("Word set's description"),
                               fieldWithPath("set.words").description("Array of words"),
                               fieldWithPath("_links").ignored()
                       ), links(
                               linkWithRel("self").description("Link to the word set"),
                               linkWithRel("wordSets").description("Link to get all word sets for the user")
                       ), pathParameters(
                               parameterWithName("id").description("The word set's id")
                       ), headersSnippet
               ));
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
        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername(id, getAuthenticatedUser().getUsername());

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();

        assertEquals(id, wordSet.getId().longValue());
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
        assertThat(wordSet.getStudiedWords(), emptyCollectionOf(StudiedWord.class));
    }

    @Test
    public void createWordSetWithWords() throws Exception {
        String name = "createdWs";
        String description = "some desc";
        WordSetDto wordSetDto = new WordSetDto(null, name, description, new ArrayList<>(Arrays.asList(
                new WordDto(null, "book1", "книга1", null, "img1", null),
                new WordDto(null, "book2", "книга2", null, "img2", null)
        )));

        MvcResult mvcResult = mockMvc.perform(post("/api/sets")
                                                      .with(userToken())
                                                      .contentType(contentType)
                                                      .content(jsonConverter.toJson(wordSetDto)))
                                     .andExpect(status().isCreated())
                                     .andExpect(header().string("Location", is(notNullValue())))
                                     .andDo(documentationHandler.document(
                                             requestFields(
                                                     fieldWithPath("id").ignored(),
                                                     fieldWithPath("name").description("Word set's name"),
                                                     fieldWithPath("description").description("Word set's description"),
                                                     fieldWithPath("words").description("Array of words")
                                             ), headersSnippet
                                     )).andReturn();

        long id = extractIdFromLocationHeader(mvcResult);
        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername(id, getAuthenticatedUser().getUsername());

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();

        assertEquals(id, wordSet.getId().longValue());
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
        List<StudiedWord> studiedWords = wordSet.getStudiedWords();
        assertThat(studiedWords, hasSize(2));

        for (StudiedWord book : studiedWords) {
            assertNotNull(book.getId());
            assertNotNull(book.getSound());
            assertEquals(WordStage.NOT_LEARNED, book.getStage());
        }
    }

    @Test
    public void createWordSetWithIllegalWords() throws Exception {
        String name = "createdWs";
        String description = "some desc";
        WordSetDto wordSetDto = new WordSetDto(null, name, description, new ArrayList<>(Arrays.asList(
                new WordDto(null, "book1", "книга1", null, "img1", null),
                new WordDto(null, "book2", "книга2", WordStage.LEARNING, "img2", null)
        )));

        mockMvc.perform(post("/api/sets")
                                .with(userToken())
                                .contentType(contentType)
                                .content(jsonConverter.toJson(wordSetDto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteWordSet() throws Exception {
        mockMvc.perform(delete("/api/sets/{id}", wordSetIds[0])
                                .with(userToken()))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       pathParameters(
                               parameterWithName("id").description("Word set's id")
                       ), headersSnippet
               ));

        Optional<UserDictionary> dictionaryOptional = userDictionaryService.getByUsername(
                getAuthenticatedUser().getUsername());

        assertTrue(dictionaryOptional.isPresent());
        UserDictionary userDictionary = dictionaryOptional.get();

        assertTrue(userDictionary.getWordSets().stream().noneMatch(wordSet -> wordSetIds[0] == wordSet.getId()));

        List<Long> wordList = wordService.listAll().stream().map(StudiedWord::getId).collect(Collectors.toList());

        assertEquals(3, wordList.size());
        assertTrue(wordList.stream().noneMatch(wordId -> wordId == 0L));
        assertTrue(wordList.stream().noneMatch(wordId -> wordId == 1L));
    }

    @Test
    public void deleteWordFromWordSet() throws Exception {
        mockMvc.perform(delete("/api/sets/{wordSetId}/words/{wordId}", wordSetIds[0], wordIds[0])
                                .with(userToken()))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       pathParameters(
                               parameterWithName("wordSetId").description("Word set's id"),
                               parameterWithName("wordId").description("Word's id")
                       ), headersSnippet
               ));

        Optional<WordSet> wordSetOptional = wordSetService.getByIdAndUsername((long) wordSetIds[0],
                                                                              getAuthenticatedUser().getUsername());

        assertTrue(wordSetOptional.isPresent());
        WordSet wordSet = wordSetOptional.get();

        assertTrue(wordSet.getStudiedWords().stream().noneMatch(word -> word.getId() == wordIds[0]));

        Optional<StudiedWord> wordOptional = wordService.getById((long) wordIds[0]);
        assertFalse(wordOptional.isPresent());
    }

    @Test
    public void addWordToWordSet() throws Exception {
        WordDto wordDto = new WordDto(null, "book1", "книга1", null, "img1", null);

        MvcResult mvcResult = mockMvc.perform(post("/api/sets/{id}/words", wordSetIds[0])
                                                      .with(userToken())
                                                      .contentType(contentType)
                                                      .content(jsonConverter.toJson(wordDto)))
                                     .andExpect(status().isCreated())
                                     .andExpect(header().string("Location", is(notNullValue())))
                                     .andDo(documentationHandler.document(
                                             pathParameters(
                                                     parameterWithName("id").description("Word set's id")
                                             ), requestFields(
                                                     fieldWithPath("id").ignored(),
                                                     fieldWithPath("text").description("Word's text in English"),
                                                     fieldWithPath("translation").description("Word's translation"),
                                                     fieldWithPath("stage").ignored(),
                                                     fieldWithPath("image").description("Path to the word's image"),
                                                     fieldWithPath("sound").ignored()
                                             ), headersSnippet
                                     ))
                                     .andReturn();

        long id = extractIdFromLocationHeader(mvcResult);
        Optional<StudiedWord> studiedWordOptional = wordSetService
                .getByIdAndUsername((long) wordSetIds[0], getAuthenticatedUser().getUsername())
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
    public void addIllegalWordToWordSet() throws Exception {
        WordDto wordDto = new WordDto(null, "book1", "книга1", WordStage.LEARNING, "img1", null);

        mockMvc.perform(post("/api/sets/{wordSetId}/words", wordSetIds[0])
                                .with(userToken())
                                .contentType(contentType)
                                .content(jsonConverter.toJson(wordDto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void addTheSameWordToWordSet() throws Exception {
        WordDto wordDto = new WordDto(null, "word1", "слово1", null, null, null);

        mockMvc.perform(post("/api/sets/{wordSetId}/words", wordSetIds[0])
                                .with(userToken())
                                .contentType(contentType)
                                .content(jsonConverter.toJson(wordDto)))
               .andExpect(status().isConflict());
    }

    @Test
    public void updateWordSetMetaInformation() throws Exception {
        String name = "updated name";
        String description = "updated description";
        WordSetMetaDto metaDto = new WordSetMetaDto(name, description);

        mockMvc.perform(patch("/api/sets/{id}", wordSetIds[0])
                                .with(userToken())
                                .contentType(contentType)
                                .content(jsonConverter.toJson(metaDto)))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       pathParameters(
                               parameterWithName("id").description("Word set's id")
                       ), requestFields(
                               fieldWithPath("name").description("New word set's name"),
                               fieldWithPath("description").description("New word set's description")
                       ), headersSnippet
               ));

        Optional<WordSet> wordSetOptional = wordSetService.getById((long) wordSetIds[0]);

        assertTrue(wordSetOptional.isPresent());

        WordSet wordSet = wordSetOptional.get();
        assertEquals(name, wordSet.getName());
        assertEquals(description, wordSet.getDescription());
    }

    private long extractIdFromLocationHeader(MvcResult mvcResult) {
        String location = mvcResult.getResponse().getHeader("Location");
        String[] urlChunks = location.split("/");
        return Long.parseLong(urlChunks[urlChunks.length - 1]);
    }
}
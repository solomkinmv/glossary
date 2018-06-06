package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import io.github.solomkinmv.glossary.words.service.practice.repetition.RepetitionTest;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest.Question;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PracticeControllerSystemTest extends BaseTest {

    private WordSetResponse wordSet1;
    private WordSetResponse wordSet2;

    @MockBean
    private TtsClient ttsClient;
    private Map<String, WordResponse> wordsMap;

    @Before
    public void mockTts() {
        when(ttsClient.getSpeech(anyString()))
                .thenReturn(new SpeechResult("speech-url"));
    }

    @Before
    public void setUp() throws Exception {
        wordSet1 = createWordSet();
        wordSet2 = createWordSet();

        wordsMap = Stream.concat(wordSet1.getWords().stream(), wordSet2.getWords().stream())
                         .collect(Collectors.toMap(WordResponse::getText, Function.identity()));
    }

    private WordSetResponse createWordSet() throws Exception {
        String name = "word-set-name";
        String description = "desc";
        WordSetMeta wordSetMeta = new WordSetMeta(userId.get(), name, description);
        long wordSetId = createWordSet(wordSetMeta);


        IntStream.rangeClosed(1, 9)
                 .mapToObj(i -> new WordMeta(wordSetId + "word" + i, wordSetId + "translation" + i, "img-url"))
                 .forEach(word -> addWordToWordSet(wordSetId, word));

        return getWordSetById(wordSetId);
    }

    @Test
    public void handlesTestResults() throws Exception {
        WordResponse word0 = wordSet1.getWords().get(0);
        WordResponse word1 = wordSet1.getWords().get(1);
        WordResponse word2 = wordSet1.getWords().get(2);

        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        WordSetResponse updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(2).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 2
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNED);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 3
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), false
        ));
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults)))
               .andExpect(status().isOk());
        updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.NOT_LEARNED);
    }

    @Test
    public void generatesGenericTest() throws Exception {
        mockMvc.perform(get("/practices/generic")
                                .param("userId", valueOf(userId.get())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.words", hasSize(AbstractTestProvider.TEST_SIZE)));
    }

    @Test
    public void generatesQuiz() throws Exception {
        mockMvc.perform(get("/practices/quiz")
                                .param("userId", valueOf(userId.get()))
                                .param("originQuestions", "true"))
               .andDo(print())
               .andExpect(jsonPath("$.questions", hasSize(AbstractTestProvider.TEST_SIZE)))
               .andExpect(status().isOk());
    }

    @Test
    public void generatesWritingTestWithAppropriateNumberOfQuestions() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(true);

        long numberOfUniqueQuestions = writingPracticeTest.getQuestions().stream()
                                                          .map(Question::getQuestionText)
                                                          .distinct()
                                                          .count();
        assertThat(writingPracticeTest.getQuestions())
                .hasSize(AbstractTestProvider.TEST_SIZE);
        assertThat(numberOfUniqueQuestions).isEqualTo(AbstractTestProvider.TEST_SIZE);
    }

    @Test
    public void generatesWritingTestWithAppropriateAnswers() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(true);

        writingPracticeTest.getQuestions().forEach(question -> {
            String expectedTranslation = wordsMap.get(question.getQuestionText()).getTranslation();

            String actualAnswer = question.getAnswer().getAnswerText();

            assertThat(actualAnswer).isEqualTo(expectedTranslation);
        });
    }

    @Test
    public void generatesWritingTestWithOriginAsAnswers() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(false);

        writingPracticeTest.getQuestions().forEach(question -> {
            String expectedOrigin = wordsMap.get(question.getAnswer().getAnswerText()).getText();

            String actualAnswer = question.getAnswer().getAnswerText();

            assertThat(actualAnswer).isEqualTo(expectedOrigin);
        });
    }

    @Test
    public void generatesWritingTestFromAllSets() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(true);

        List<String> wordsFromWordSet1 = extractWords(wordSet1);
        List<String> wordsFromWordSet2 = extractWords(wordSet2);
        assertThat(writingPracticeTest.getQuestions())
                .extracting(Question::getQuestionText)
                .containsAnyElementsOf(wordsFromWordSet1)
                .containsAnyElementsOf(wordsFromWordSet2);
    }

    private List<String> extractWords(WordSetResponse wordSet) {
        return wordSet.getWords()
                      .stream()
                      .map(WordResponse::getText)
                      .collect(Collectors.toList());
    }

    @Test
    public void generatesWritingTestForAppropriateWordSet() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(wordSet1.getId());

        List<String> wordsFromWordSet2 = extractWords(wordSet2);
        assertThat(writingPracticeTest.getQuestions())
                .extracting(Question::getQuestionText)
                .doesNotContainAnyElementsOf(wordsFromWordSet2);
    }

    @Test
    public void generatesRepetitionTest() {
        Assert.fail();
    }

    private WritingPracticeTest getWritingPracticeTest(long wordSetId) throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/writing")
                                                         .param("userId", valueOf(userId.get()))
                                                         .param("originQuestions", valueOf(true))
                                                         .param("setId", valueOf(wordSetId))
        )
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, WritingPracticeTest.class);
    }

    private WritingPracticeTest getWritingPracticeTest(boolean originQuestions) throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/writing")
                                                         .param("userId", valueOf(userId.get()))
                                                         .param("originQuestions", valueOf(originQuestions)))
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, WritingPracticeTest.class);
    }

    private RepetitionTest getRepetition(boolean originQuestion) throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/repetition")
                                                         .param("userId", valueOf(userId.get()))
                                                         .param("originQuestions", valueOf(originQuestion)))
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, RepetitionTest.class);
    }
}
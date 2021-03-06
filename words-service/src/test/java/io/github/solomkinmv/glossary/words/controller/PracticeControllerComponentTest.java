package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.WithOAuthSubject;
import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.Answer;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.PracticeType;
import io.github.solomkinmv.glossary.words.service.practice.generic.GenericTest;
import io.github.solomkinmv.glossary.words.service.practice.quiz.Quiz;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest;
import io.github.solomkinmv.glossary.words.service.practice.writing.WritingPracticeTest.Question;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.solomkinmv.glossary.words.service.practice.PracticeType.LEARNED_FIRST;
import static io.github.solomkinmv.glossary.words.service.practice.PracticeType.LEARNING;
import static io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider.NUMBER_OF_CHOICES;
import static io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider.TEST_SIZE;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PracticeControllerComponentTest extends BaseTest {

    @Test
    @WithOAuthSubject
    public void handlesTestResults() throws Exception {
        WordResponse word0 = wordSet1.getWords().get(0);
        WordResponse word1 = wordSet1.getWords().get(1);
        WordResponse word2 = wordSet1.getWords().get(2);

        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), false
        ));
        handleResults(practiceResults);
        WordSetResponse updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.LEARNING);
        assertThat(updatedWordSet.getWords().get(2).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 2
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), false
        ));
        handleResults(practiceResults);
        updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.LEARNED);
        assertThat(updatedWordSet.getWords().get(1).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // iteration 3
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), false
        ));
        handleResults(practiceResults);
        updatedWordSet = getWordSetById(wordSet1.getId());

        assertThat(updatedWordSet.getWords().get(0).getStage()).isEqualTo(WordStage.NOT_LEARNED);

        // basically this request used only to help Spring Rest Docs to record request and response body
        practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), false
        ));
        handleResults(practiceResults);
    }

    private void handleResults(PracticeResults practiceResults) throws Exception {
        mockMvc.perform(post("/practices")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(practiceResults))
                                .header(AUTHORIZATION, "Bearer foo"))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestHeaders(
                               headerWithName("Authorization").description("OAuth2 JWT token")),
                       requestFields(
                               fieldWithPath("wordAnswers.*")
                                       .description(
                                               "Map of word answers. Key is word id, value is boolean successful result")
                       )
               ));
    }

    @Test
    @WithOAuthSubject
    public void generatesGenericTestForWordSet() throws Exception {
        WordResponse word0 = wordSet1.getWords().get(0);
        WordResponse word1 = wordSet1.getWords().get(1);
        WordResponse word2 = wordSet1.getWords().get(2);
        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), true
        ));
        handleResults(practiceResults);
        handleResults(practiceResults);

        GenericTest genericTest = getGenericTest(wordSet1.getId(), false);

        assertThat(genericTest.getWords())
                .extracting(GenericTest.GenericTestWord::getWordId)
                .doesNotContain(word0.getId(), word1.getId(), word2.getId())
                .hasSize(wordSet1.getWords().size() - practiceResults.getWordAnswers().size());
    }

    @Test
    @WithOAuthSubject
    public void generatesGenericTestWithLearnedWords() throws Exception {
        WordResponse word0 = wordSet1.getWords().get(0);
        WordResponse word1 = wordSet1.getWords().get(1);
        WordResponse word2 = wordSet1.getWords().get(2);
        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), true
        ));
        handleResults(practiceResults);
        handleResults(practiceResults);

        GenericTest genericTest = getGenericTest(wordSet1.getId(), false, LEARNED_FIRST);

        assertThat(genericTest.getWords().subList(0, practiceResults.getWordAnswers().size()))
                .extracting(GenericTest.GenericTestWord::getWordId)
                .contains(word0.getId(), word1.getId(), word2.getId());

        assertThat(genericTest.getWords())
                .hasSize(wordSet1.getWords().size());
    }

    @Test
    @WithOAuthSubject
    public void generatesGenericTestWithAllWords() throws Exception {
        WordResponse word0 = wordSet1.getWords().get(0);
        WordResponse word1 = wordSet1.getWords().get(1);
        WordResponse word2 = wordSet1.getWords().get(2);
        PracticeResults practiceResults = new PracticeResults(Map.of(
                word0.getId(), true,
                word1.getId(), true,
                word2.getId(), true
        ));
        handleResults(practiceResults);
        handleResults(practiceResults);

        GenericTest genericTest = getGenericTest(wordSet1.getId(), false, LEARNED_FIRST);

        assertThat(genericTest.getWords())
                .extracting(GenericTest.GenericTestWord::getWordId)
                .hasSize(wordSet1.getWords().size());
    }

    @Test
    @WithOAuthSubject
    public void generatesGenericTestForAllWordSetsWithTrueOrigin() throws Exception {
        GenericTest genericTest = getGenericTest(wordSet1.getId(), true);

        assertThat(genericTest.getWords())
                .extracting(GenericTest.GenericTestWord::getText)
                .contains(wordSet1.getWords().stream().map(WordResponse::getText).toArray(String[]::new));
    }

    private GenericTest getGenericTest(long setId, boolean originQuestions) throws Exception {
        return getGenericTest(setId, originQuestions, LEARNING);
    }

    private GenericTest getGenericTest(long setId, boolean originQuestions, PracticeType practiceType) throws Exception {
        String json = mockMvc.perform(get("/practices/generic")
                                              .param("setId", valueOf(setId))
                                              .param("originQuestions", valueOf(originQuestions))
                                              .param("practiceType", valueOf(practiceType))
                                              .header(AUTHORIZATION, "Bearer foo"))
                             .andExpect(status().isOk())
                             .andDo(documentationHandler.document(
                                     requestHeaders(
                                             headerWithName("Authorization").description("OAuth2 JWT token")),
                                     requestParameters(
                                             parameterWithName("setId").description("Word Set id to generate generic test"),
                                             parameterWithName("originQuestions")
                                                     .description(
                                                             "Flag that specifies test direction (word -> translation or translation -> word)"),
                                             parameterWithName("practiceType")
                                                     .description("Describes what types of words should be included in test scope. Possible values: " +
                                                                          Arrays.toString(PracticeType.values()))
                                     ), responseFields(
                                             fieldWithPath("words[].wordId").description("id of the word"),
                                             fieldWithPath("words[].text").description("Original text of the word"),
                                             fieldWithPath("words[].translation").description("Translation text of the word"),
                                             fieldWithPath("words[].stage").description("Learning stage of the word"),
                                             fieldWithPath("words[].image").description("Image url for the word"),
                                             fieldWithPath("words[].sound").description("Pronunciation sound url for the word")
                                     )
                             )).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, GenericTest.class);
    }

    @Test
    @WithOAuthSubject
    public void generatesQuizOfCorrectSize() throws Exception {
        Quiz quiz = getQuiz();

        assertThat(quiz.getQuestions()).hasSize(TEST_SIZE);
    }

    @Test
    @WithOAuthSubject
    public void generatesQuizWithCorrectAnswers() throws Exception {
        Quiz quiz = getQuiz();

        quiz.getQuestions().forEach(question -> {
            WordResponse associatedWord = wordsMap.get(question.getQuestionText());
            Answer expectedAnswer = new Answer(associatedWord.getId(),
                                               associatedWord.getTranslation(),
                                               associatedWord.getStage(),
                                               associatedWord.getImage(),
                                               associatedWord.getSound());

            assertThat(question.getAnswer())
                    .isEqualTo(expectedAnswer);
        });
    }

    @Test
    @WithOAuthSubject
    public void generatesQuizWithOneCorrectAlternative() throws Exception {
        Quiz quiz = getQuiz();

        quiz.getQuestions().forEach(question -> {
            WordResponse associatedWord = wordsMap.get(question.getQuestionText());

            assertThat(question.getAlternatives()).hasSize(NUMBER_OF_CHOICES);
            assertThat(question.getAlternatives()).containsOnlyOnce(associatedWord.getTranslation());
        });
    }

    private Quiz getQuiz() throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/quiz")
                                                         .param("originQuestions", valueOf(true))
                                                         .header(AUTHORIZATION, "Bearer foo"))
                                        .andExpect(status().isOk())
                                        .andDo(documentationHandler.document(
                                                requestHeaders(
                                                        headerWithName("Authorization").description("OAuth2 JWT token")),
                                                requestParameters(
                                                        parameterWithName("originQuestions")
                                                                .description(
                                                                        "Flag that specifies test direction (word -> translation or translation -> word)"),
                                                        parameterWithName("setId")
                                                                .description("Optional word set id")
                                                                .optional()
                                                ),
                                                responseFields(
                                                        fieldWithPath("questions[].questionText")
                                                                .description("Question text"),
                                                        fieldWithPath("questions[].answer.wordId")
                                                                .description("Correct word id"),
                                                        fieldWithPath("questions[].answer.answerText")
                                                                .description("Correct word text"),
                                                        fieldWithPath("questions[].answer.stage")
                                                                .description("Correct word learning stage"),
                                                        fieldWithPath("questions[].answer.image")
                                                                .description("Correct word image url"),
                                                        fieldWithPath("questions[].answer.pronunciation")
                                                                .description("Correct word pronunciation url"),
                                                        fieldWithPath("questions[].alternatives[]")
                                                                .description(
                                                                        "List of possible answers. Includes one correct answer")
                                                )
                                        ))
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, Quiz.class);
    }

    @Test
    @WithOAuthSubject
    public void generatesWritingTestWithAppropriateNumberOfQuestions() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(true);

        long numberOfUniqueQuestions = writingPracticeTest.getQuestions().stream()
                                                          .map(Question::getQuestionText)
                                                          .distinct()
                                                          .count();
        assertThat(writingPracticeTest.getQuestions())
                .hasSize(TEST_SIZE);
        assertThat(numberOfUniqueQuestions).isEqualTo(TEST_SIZE);
    }

    @Test
    @WithOAuthSubject
    public void generatesWritingTestWithAppropriateAnswers() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(true);

        writingPracticeTest.getQuestions().forEach(question -> {
            String expectedTranslation = wordsMap.get(question.getQuestionText()).getTranslation();

            String actualAnswer = question.getAnswer().getAnswerText();

            assertThat(actualAnswer).isEqualTo(expectedTranslation);
        });
    }

    @Test
    @WithOAuthSubject
    public void generatesWritingTestWithOriginAsAnswers() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(false);

        writingPracticeTest.getQuestions().forEach(question -> {
            String expectedOrigin = wordsMap.get(question.getAnswer().getAnswerText()).getText();

            String actualAnswer = question.getAnswer().getAnswerText();

            assertThat(actualAnswer).isEqualTo(expectedOrigin);
        });
    }

    @Test
    @WithOAuthSubject
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
    @WithOAuthSubject
    public void generatesWritingTestForAppropriateWordSet() throws Exception {
        WritingPracticeTest writingPracticeTest = getWritingPracticeTest(wordSet1.getId());

        List<String> wordsFromWordSet2 = extractWords(wordSet2);
        assertThat(writingPracticeTest.getQuestions())
                .extracting(Question::getQuestionText)
                .doesNotContainAnyElementsOf(wordsFromWordSet2);
    }

    private WritingPracticeTest getWritingPracticeTest(long wordSetId) throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/writing")
                                                         .param("originQuestions", valueOf(true))
                                                         .param("setId", valueOf(wordSetId))
                                                         .header(AUTHORIZATION, "Bearer foo"))
                                        .andExpect(status().isOk())
                                        .andDo(documentationHandler.document(
                                                requestHeaders(
                                                        headerWithName("Authorization").description("OAuth2 JWT token")),
                                                requestParameters(
                                                        parameterWithName("originQuestions")
                                                                .description(
                                                                        "Flag that specifies test direction (word -> translation or translation -> word)"),
                                                        parameterWithName("setId")
                                                                .description("Optional word set id")
                                                                .optional()
                                                ),
                                                responseFields(
                                                        fieldWithPath("questions[].questionText")
                                                                .description("Question text"),
                                                        fieldWithPath("questions[].answer.wordId")
                                                                .description("Correct word id"),
                                                        fieldWithPath("questions[].answer.answerText")
                                                                .description("Correct word text"),
                                                        fieldWithPath("questions[].answer.stage")
                                                                .description("Correct word learning stage"),
                                                        fieldWithPath("questions[].answer.image")
                                                                .description("Correct word image url"),
                                                        fieldWithPath("questions[].answer.pronunciation")
                                                                .description("Correct word pronunciation url")
                                                )
                                        ))
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, WritingPracticeTest.class);
    }

    private WritingPracticeTest getWritingPracticeTest(boolean originQuestions) throws Exception {
        String contentAsString = mockMvc.perform(get("/practices/writing")
                                                         .param("originQuestions", valueOf(originQuestions))
                                                         .header(AUTHORIZATION, "Bearer foo"))
                                        .andExpect(status().isOk())
                                        .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, WritingPracticeTest.class);
    }
}
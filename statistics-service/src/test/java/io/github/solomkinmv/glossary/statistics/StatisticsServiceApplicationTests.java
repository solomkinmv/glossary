package io.github.solomkinmv.glossary.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.statistics.controller.dto.UserStatsResponse;
import io.github.solomkinmv.glossary.statistics.domain.WordStage;
import io.github.solomkinmv.glossary.statistics.listener.LearningResultMessage;
import io.github.solomkinmv.glossary.statistics.listener.LearningResultMessage.LearningResult;
import lombok.SneakyThrows;
import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.atomic.AtomicLong;

import static io.github.solomkinmv.glossary.statistics.domain.WordStage.LEARNED;
import static io.github.solomkinmv.glossary.statistics.domain.WordStage.LEARNING;
import static io.github.solomkinmv.glossary.statistics.domain.WordStage.NOT_LEARNED;
import static io.github.solomkinmv.glossary.statistics.listener.LearningResultMessage.LearningResult.CORRECT;
import static io.github.solomkinmv.glossary.statistics.listener.LearningResultMessage.LearningResult.INCORRECT;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
public class StatisticsServiceApplicationTests {

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    private Sink input;
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        counter.incrementAndGet();
    }

    @Test
    public void returnsCorrectResultIfNoStatsForUser() {
        assertCounters(0, 0);
    }

    @Test
    public void countsMultipleWordStats() {
        input.input().send(createMessage(createLearningResult(LEARNED, CORRECT, 1)));
        input.input().send(createMessage(createLearningResult(LEARNING, CORRECT, 2)));
        input.input().send(createMessage(createLearningResult(NOT_LEARNED, INCORRECT, 3)));
        assertCounters(1, 3);
    }

    @Test
    public void doesNotCountWordSecondTimeIfLearned() {
        int wordId = 1;
        input.input().send(createMessage(createLearningResult(LEARNED, CORRECT, wordId)));
        assertCounters(1, 1);

        input.input().send(createMessage(createLearningResult(LEARNING, INCORRECT, wordId)));
        assertCounters(1, 1);
    }

    @Test
    public void learnedWordCountsAsLearnedAndLearning() {
        int wordId = 1;

        input.input().send(createMessage(createLearningResult(LEARNED, CORRECT, wordId)));
        assertCounters(1, 1);
    }

    @Test
    public void learningWordCountsOnlyAsLearning() {
        int wordId = 1;

        input.input().send(createMessage(createLearningResult(LEARNING, CORRECT, wordId)));
        assertCounters(0, 1);
    }

    @Test
    public void removesWordFromLearnedIfUserAnsweredIncorrectly() {
        int wordId = 1;

        input.input().send(createMessage(createLearningResult(LEARNED, CORRECT, wordId)));
        assertCounters(1, 1);

        input.input().send(createMessage(createLearningResult(NOT_LEARNED, INCORRECT, wordId)));
        assertCounters(0, 1);
    }

    public String getSubjectId() {
        return "subjectId" + counter.get();
    }

    @SneakyThrows
    private Message<String> createMessage(LearningResultMessage learningResultMessage) {
        return MessageBuilder.withPayload(objectMapper.writeValueAsString(learningResultMessage)).build();
    }

    private LearningResultMessage createLearningResult(WordStage learned, LearningResult correct, long wordId) {
        return LearningResultMessage.builder()
                                    .subjectId(getSubjectId())
                                    .wordId(wordId)
                                    .stage(learned)
                                    .result(correct)
                                    .build();
    }

    private void assertCounters(int learnedWords, int learningWords) {
        await()
                .atMost(Duration.ONE_SECOND)
                .untilAsserted(() -> getStats()
                        .isEqualTo(UserStatsResponse.builder()
                                                    .subjectId(getSubjectId())
                                                    .learnedWords(learnedWords)
                                                    .learningWords(learningWords)
                                                    .build()));
    }

    private WebTestClient.BodySpec<UserStatsResponse, ?> getStats() {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/")
                                                           .queryParam("subjectId", getSubjectId())
                                                           .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(UserStatsResponse.class);
    }
}

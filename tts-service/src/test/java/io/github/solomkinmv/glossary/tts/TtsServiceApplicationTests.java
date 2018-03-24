package io.github.solomkinmv.glossary.tts;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import io.github.solomkinmv.glossary.storage.client.StorageClient;
import io.github.solomkinmv.glossary.storage.client.StoredType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TtsServiceApplicationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private RestDocumentationResultHandler documentationHandler;
    @MockBean
    private StorageClient storageClient;
    @Autowired
    private AmazonPolly amazonPolly;

    @Before
    public void setUp() {
        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();
    }

    @Test
    public void retrievesAlreadyPresentSoundFile() throws Exception {
        String url = "some-url";
        when(storageClient.get(eq(StoredType.SOUND), any()))
                .thenReturn(Optional.of(url));

        mockMvc.perform(get("")
                                .param("text", "some text"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.url").value(url))
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("text").description("Text to generate speech record")
                       ), responseFields(
                               fieldWithPath("url").description("Url of the generates speech record")
                       )
               ));
    }

    @Test
    public void savesGeneratedSpeechRecordIfNotFoundInStorage() throws Exception {
        String url = "some-url";
        when(storageClient.get(StoredType.SOUND, "sometext.mp3"))
                .thenReturn(Optional.empty());
        when(storageClient.save(eq(StoredType.SOUND), any()))
                .thenReturn(ResponseEntity.created(URI.create(url)).build());

        mockMvc.perform(get("")
                                .param("text", "some text"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.url").value(url))
               .andReturn()
               .getResponse()
               .getContentAsString();
    }

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public AmazonPolly amazonPolly() {
            AmazonPolly mock = mock(AmazonPolly.class);

            DescribeVoicesResult describeVoicesResult = new DescribeVoicesResult();
            describeVoicesResult.setVoices(singletonList(new Voice()));
            when(mock.describeVoices(any()))
                    .thenReturn(describeVoicesResult);

            SynthesizeSpeechResult synthesizeSpeechResult = new SynthesizeSpeechResult();
            synthesizeSpeechResult.setAudioStream(new InputStream() {
                @Override
                public int read() {
                    return -1;
                }
            });
            when(mock.synthesizeSpeech(any()))
                    .thenReturn(synthesizeSpeechResult);

            return mock;
        }
    }
}

package io.github.solomkinmv.glossary.storage.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static wiremock.org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static wiremock.org.eclipse.jetty.http.HttpStatus.OK_200;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = StorageClientTest.TestConfiguration.class)
@AutoConfigureWireMock(port = 8081)
public class StorageClientTest {

    private String responseBody = "http://localhost:8080/img/image.png";

    @Autowired
    private StorageClient storageClient;

    @Test
    public void getsUrlByTypeAndFilename() {
        stubFor(get(urlPathEqualTo("/"))
                        .withQueryParam("type", equalTo("IMG"))
                        .withQueryParam("filename", equalTo("image.png"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(responseBody)));

        Optional<String> response = storageClient.get(StoredType.IMG, "image.png");

        assertThat(response).contains(responseBody);
    }

    @Test
    public void returnsEmptyOptionalIfNoContent() {
        stubFor(get(urlPathEqualTo("/"))
                        .withQueryParam("type", equalTo("IMG"))
                        .withQueryParam("filename", equalTo("image.png"))
                        .willReturn(aResponse()
                                            .withStatus(NO_CONTENT_204)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(responseBody)));

        Optional<String> optionalResponse = storageClient.get(StoredType.IMG, "image.png");

        assertThat(optionalResponse).isEmpty();
    }

    @Test
    public void deletesByType() {
        stubFor(delete(urlPathEqualTo("/"))
                        .withQueryParam("type", equalTo("SOUND"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)));

        ResponseEntity<Void> response = storageClient.delete(StoredType.SOUND);

        assertThat(response.getStatusCode().value()).isEqualTo(OK_200);
    }

    @Test
    public void deletesByTypeAndFilename() {
        stubFor(delete(urlPathEqualTo("/"))
                        .withQueryParam("type", equalTo("SOUND"))
                        .withQueryParam("filename", equalTo("book.mp3"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)));

        ResponseEntity<Void> response = storageClient.delete(StoredType.SOUND, "book.mp3");

        assertThat(response.getStatusCode().value()).isEqualTo(OK_200);
    }

    @Test
    public void savesMultipartFile() {
        String imagePngContentType = "image/png";
        String fileContent = "nonsensecontent";
        String location = "location";
        stubFor(any(urlPathEqualTo("/"))
                        .withQueryParam("type", equalTo("IMG"))
                        .withMultipartRequestBody(
                                aMultipart()
                                        .withName("file")
                                        .withHeader("Content-Type", containing(imagePngContentType))
                                        .withBody(binaryEqualTo(fileContent.getBytes())))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withHeader("Location", location)));
        MockMultipartFile file = new MockMultipartFile("file", "image.png", imagePngContentType,
                                                       fileContent.getBytes());

        ResponseEntity<Void> response = storageClient.save(StoredType.IMG, file);

        assertThat(response.getStatusCode().value()).isEqualTo(OK_200);
        assertThat(response.getHeaders().get("Location")).containsOnly(location);
    }

    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}
package io.github.solomkinmv.glossary.storage;

import io.github.solomkinmv.glossary.storage.properties.StorageProperties;
import io.github.solomkinmv.glossary.storage.service.StoredType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StorageControllerSystemTest {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");
    private RestDocumentationResultHandler documentationHandler;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private String imgPathPrefix;
    @Autowired
    private StorageProperties storageProperties;
    private Path imgUploadPath;

    @After
    public void cleanUp() throws IOException {
        if (Files.exists(imgUploadPath)) {
            Files.walkFileTree(imgUploadPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        }
    }

    @Before
    public void setUpMockMvc() {
        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();

        imgPathPrefix = storageProperties.getImgUrlPrefix();
        imgUploadPath = Paths.get(storageProperties.getImgUploadDir());
    }

    private void createInitialTestFile(String filename, String content) throws IOException {

        if (!Files.exists(imgUploadPath)) {
            Files.createDirectory(imgUploadPath);
        }

        Path filePath = imgUploadPath.resolve(filename);
        Files.createFile(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(content);
        }
    }

    @Test
    public void uploadsFile() throws Exception {
        String originalFilename = "img one.jpg";
        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/png",
                                                       "nonsensecontent".getBytes());
        mockMvc.perform(multipart("/")
                                .file(file)
                                .param("type", "IMG")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location",
                                          equalTo("http://localhost:8080" + imgPathPrefix + "/imgone.jpg")))
               .andDo(documentationHandler.document(
                       requestParts(
                               partWithName("file").description("A file to upload")
                       ),
                       requestParameters(
                               parameterWithName("type")
                                       .description(
                                               "A file type. Could be on of: " + Arrays.toString(StoredType.values()))
                       )
               ));
    }

    @Test
    public void getsFile() throws Exception {
        String originalFilename = "file.png";
        String content = "nonsensecontent";
        String storedTypes = Arrays.toString(StoredType.values());
        createInitialTestFile(originalFilename, content);

        mockMvc.perform(get("/")
                                .param("type", "IMG")
                                .param("filename", originalFilename))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("type")
                                       .description("A file type. Could be on of: " + storedTypes),
                               parameterWithName("filename")
                                       .description("Filename to get")
                       )
               ));
    }

    @Test
    public void deletesExistingFile() throws Exception {
        String originalFilename = "fileToDelete.png";
        String content = "nonsensecontent";
        String storedTypes = Arrays.toString(StoredType.values());
        createInitialTestFile(originalFilename, content);

        mockMvc.perform(delete("/")
                                .param("type", "IMG")
                                .param("filename", originalFilename))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("type")
                                       .description("A file type. Could be on of: " + storedTypes),
                               parameterWithName("filename")
                                       .description("Filename to get")
                       )
               ));

        Path filePath = imgUploadPath.resolve(originalFilename);
        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    public void returnsOkIfDeletesAbsentFile() throws Exception {
        String originalFilename = "fileToDelete.png";
        String storedTypes = Arrays.toString(StoredType.values());

        mockMvc.perform(delete("/")
                                .param("type", "IMG")
                                .param("filename", originalFilename))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("type")
                                       .description("A file type. Could be on of: " + storedTypes),
                               parameterWithName("filename")
                                       .description("Filename to get")
                       )
               ));

        Path filePath = imgUploadPath.resolve(originalFilename);
        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    public void deletesExistingFilesByType() throws Exception {
        String originalFilenameOne = "fileToDelete1.png";
        String originalFilenameTwo = "fileToDelete2.png";
        String content = "nonsensecontent";
        String storedTypes = Arrays.toString(StoredType.values());
        createInitialTestFile(originalFilenameOne, content);
        createInitialTestFile(originalFilenameTwo, content);

        mockMvc.perform(delete("/")
                                .param("type", "IMG"))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("type")
                                       .description("A file type. Could be on of: " + storedTypes)
                       )
               ));

        Path filePathOne = imgUploadPath.resolve(originalFilenameOne);
        assertThat(Files.exists(filePathOne)).isFalse();
        Path filePathTwo = imgUploadPath.resolve(originalFilenameTwo);
        assertThat(Files.exists(filePathTwo)).isFalse();
    }
}
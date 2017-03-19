package io.github.solomkinmv.glossary.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.service.images.ImageService;
import io.github.solomkinmv.glossary.service.images.StorageProperties;
import io.github.solomkinmv.glossary.web.Application;
import io.github.solomkinmv.glossary.web.dto.ImageDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link ImageController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ImageControllerTest {

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                        MediaType.APPLICATION_JSON.getSubtype(),
                                                        StandardCharsets.UTF_8);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ImageService imageService;
    private String imgPathPrefix;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        imageService.deleteImgDir();
    }

    @Test
    public void uploadsImage() throws Exception {
        String originalFilename = "img.jpg";
        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/png",
                                                       "nonsensecontent".getBytes());
        mockMvc.perform(fileUpload("/api/images")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", containsString(imgPathPrefix + "/" + originalFilename)));
    }

    @Test
    public void deletesImage() throws Exception {
        String originalFilename = "img.jpg";
        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/png",
                                                       "nonsensecontent".getBytes());
        mockMvc.perform(fileUpload("/api/images")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(delete("/api/images")
                                .contentType(contentType)
                                .content(json(new ImageDto(originalFilename))))
               .andExpect(status().isOk());
    }

    private String json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Autowired
    public void setImgUploadDir(StorageProperties storageProperties) {
        imgPathPrefix = storageProperties.getImgPrefix();
    }
}
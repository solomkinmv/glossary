package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.images.ImageService;
import io.github.solomkinmv.glossary.service.storage.StorageProperties;
import io.github.solomkinmv.glossary.web.MockMvcBase;
import io.github.solomkinmv.glossary.web.dto.ImageDto;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for {@link ImageController}.
 */
public class ImageControllerTest extends MockMvcBase {

    @Autowired
    private ImageService imageService;
    private String imgPathPrefix;

    @After
    public void tearDown() throws Exception {
        imageService.deleteImgDir();
    }

    @Test
    public void uploadsImage() throws Exception {
        String originalFilename = "img one.jpg";
        String expectedFilename = "img_one.jpg";
        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/png",
                                                       "nonsensecontent".getBytes());
        mockMvc.perform(fileUpload("/api/images")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(userToken()))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", containsString(imgPathPrefix + "/" + expectedFilename)))
               .andDo(documentationHandler.document(
                       requestParts(
                               partWithName("file").description("An image to upload")
                       ), headersSnippet
               ));
    }

    @Test
    public void deletesImage() throws Exception {
        String originalFilename = "img one.jpg";
        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/png",
                                                       "nonsensecontent".getBytes());
        mockMvc.perform(fileUpload("/api/images")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(userToken()));

        ImageDto imageDto = new ImageDto(originalFilename);
        mockMvc.perform(delete("/api/images")
                                .contentType(contentType)
                                .content(jsonConverter.toJson(imageDto))
                                .with(userToken()))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestFields(
                               fieldWithPath("image").description("Image file name")
                       ), headersSnippet
               ));
    }

    @Test
    public void failsToDeleteImageWithInvalidDto() throws Exception {
        ImageDto imageDto = new ImageDto("");
        mockMvc.perform(delete("/api/images")
                                .contentType(contentType)
                                .content(jsonConverter.toJson(imageDto))
                                .with(userToken()))
               .andExpect(status().isBadRequest());
    }

    @Autowired
    public void setImgUploadDir(StorageProperties storageProperties) {
        imgPathPrefix = storageProperties.getImgUrlPrefix();
    }
}
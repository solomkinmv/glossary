package io.github.solomkinmv.glossary.image;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import io.github.solomkinmv.glossary.storage.client.StorageClient;
import io.github.solomkinmv.glossary.storage.client.StoredType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImageServiceApplicationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private RestDocumentationResultHandler documentationHandler;
    @MockBean
    private StorageClient storageClient;
    @MockBean
    private Flickr flickr;

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
    public void searchesForImagesInFlickr() throws Exception {
        PhotosInterface value = mockPhotosInterface("url1", "url2", "url3");
        when(flickr.getPhotosInterface())
                .thenReturn(value);

        mockMvc.perform(get("/search")
                                .param("tags", "tag1,tag2,tag3"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").value("url1"))
               .andExpect(jsonPath("$[1]").value("url2"))
               .andExpect(jsonPath("$[2]").value("url3"))
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("tags").description("List of tags to search")
                       )
               ));
    }

    @Test
    public void savesImageToStorageService() throws Exception {
        String url = "some-url";
        String originalFilename = "tmp_file.png";
        MockMultipartFile file = getMockMultipartFile(originalFilename);

        when(storageClient.save(eq(StoredType.IMG), any()))
                .thenReturn(ResponseEntity.created(URI.create(url)).build());

        mockMvc.perform(multipart("/")
                                .file(file))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", equalTo(url)))
               .andDo(documentationHandler.document(
                       requestParts(
                               partWithName("file").description("An image to upload")
                       )
               ));
    }

    @Test
    public void deletesImageByName() throws Exception {
        String originalFilename = "tmp_file.png";
        when(storageClient.delete(StoredType.IMG, originalFilename))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/")
                                .param("filename", originalFilename))
               .andExpect(status().isOk())
               .andDo(documentationHandler.document(
                       requestParameters(
                               parameterWithName("filename")
                                       .description("Filename to delete")
                       )
               ));

        verify(storageClient).delete(StoredType.IMG, originalFilename);
    }

    private MockMultipartFile getMockMultipartFile(String originalFilename) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getResource("/" + originalFilename).toURI());
        byte[] bytes = Files.readAllBytes(path);
        return new MockMultipartFile("file", originalFilename, "image/png", bytes);
    }

    private PhotosInterface mockPhotosInterface(String... urls) throws FlickrException {
        PhotosInterface photosInterface = mock(PhotosInterface.class);
        PhotoList<Photo> photoList = fillPhotoList(urls);
        when(photosInterface.search(any(), anyInt(), anyInt()))
                .thenReturn(photoList);
        return photosInterface;
    }

    private PhotoList<Photo> fillPhotoList(String... urls) {
        PhotoList<Photo> photos = new PhotoList<>();
        Arrays.stream(urls)
              .map(this::mockPhoto)
              .forEach(photos::add);

        return photos;
    }

    private Photo mockPhoto(String url) {
        Photo photo = mock(Photo.class);
        when(photo.getSmallUrl())
                .thenReturn(url);
        return photo;
    }

}

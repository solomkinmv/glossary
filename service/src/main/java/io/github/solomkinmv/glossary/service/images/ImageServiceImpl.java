package io.github.solomkinmv.glossary.service.images;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import io.github.solomkinmv.glossary.service.exception.ImageExistException;
import io.github.solomkinmv.glossary.service.exception.ImageSearchException;
import io.github.solomkinmv.glossary.service.exception.ImageStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    private static final int PAGE_SIZE = 10;

    private final Flickr flickr;
    private final Path imageDir;
    private final String imgPrefix;

    @Autowired
    public ImageServiceImpl(Flickr flickr, StorageProperties storageProperties) {
        this.flickr = flickr;
        imageDir = Paths.get(storageProperties.getImgUploadDir());
        imgPrefix = storageProperties.getImgPrefix();
    }

    @PostConstruct
    private void init() {
        try {
            if (!Files.exists(imageDir)) {
                Files.createDirectory(imageDir);
            }
        } catch (IOException e) {
            String msg = "Can't create directory " + imageDir;
            log.error(msg);
            throw new ImageStoreException(msg);
        }
    }

    /**
     * Returns list of the images by the specified {@code tags}.
     * Looks for the images in the Flickr.
     *
     * @param tags tags for the images
     * @return list of image urls
     */
    @Override
    public List<String> search(String[] tags) {
        SearchParameters searchParams = new SearchParameters();
        searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);
        searchParams.setTags(tags);

        PhotosInterface photosInterface = flickr.getPhotosInterface();
        PhotoList photoList;
        try {
            photoList = photosInterface.search(searchParams, PAGE_SIZE, 0);
        } catch (FlickrException e) {
            String msg = "Exception occurred during image search";
            log.error(msg, e);
            throw new ImageSearchException(msg, e);
        }

        return IntStream.range(0, photoList.size())
                        .mapToObj(index -> (Photo) photoList.get(index))
                        .map(Photo::getSmallUrl)
                        .collect(Collectors.toList());
    }

    /**
     * Stores image {@code inputStream} to the file system.
     * Modifies {@code originalFilename} before the image storing.
     * TODO: check types (throw exception if not an image)
     *
     * @param inputStream      input stream for the image
     * @param originalFilename file name of the image
     * @return url for the stored image
     */
    @Override
    public String store(InputStream inputStream, String originalFilename) {
        String imgFilename = adaptFilename(originalFilename);
        Path imgPath = imageDir.resolve(imgFilename);
        if (Files.exists(imgPath)) {
            log.error("Image already exist: " + imgFilename);
            throw new ImageExistException("Image file name: " + imgFilename);
        }
        try {
            Files.copy(inputStream, imgPath);
        } catch (IOException e) {
            String msg = "Can't store image " + imgFilename + " to " + imageDir.toAbsolutePath();
            log.error(msg);
            throw new ImageStoreException(msg, e);
        }

        return imgPrefix + "/" + imgFilename;
    }

    /**
     * Deletes uploaded image by {@code originalFilename}.
     *
     * @param originalFilename the name of the image
     */
    @Override
    public void deleteImg(String originalFilename) {
        Path imgPath = imageDir.resolve(adaptFilename(originalFilename));
        try {
            Files.deleteIfExists(imgPath);
        } catch (IOException e) {
            log.warn("Can't remove image " + imgPath);
        }
    }

    private String adaptFilename(String originalFilename) {
        return originalFilename.replace(' ', '_');
    }
}

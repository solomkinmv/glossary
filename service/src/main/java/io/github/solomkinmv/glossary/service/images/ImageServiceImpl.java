package io.github.solomkinmv.glossary.service.images;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import io.github.solomkinmv.glossary.service.exception.ImageSearchException;
import io.github.solomkinmv.glossary.service.storage.StorageService;
import io.github.solomkinmv.glossary.service.storage.StoredType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
class ImageServiceImpl implements ImageService {

    private static final int IMAGE_WIDTH = 240;
    private final Flickr flickr;
    private final StorageService storageService;
    private final ImageProcessor imageProcessor;

    @Autowired
    public ImageServiceImpl(Flickr flickr, StorageService storageService, ImageProcessor imageProcessor) {
        this.flickr = flickr;
        this.storageService = storageService;
        this.imageProcessor = imageProcessor;
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
     *
     * @param inputStream      input stream for the image
     * @param originalFilename file name of the image
     * @return url for the stored image
     */
    @Override
    public String store(InputStream inputStream, String originalFilename) {
        log.info("Storing image with {} filename", originalFilename);
        String filename = adaptFilename(originalFilename);
        InputStream processedInputStream = imageProcessor.fitByWidth(inputStream, IMAGE_WIDTH);
        return storageService.store(processedInputStream, filename, StoredType.IMG);
    }

    @Override
    public void deleteImg(String originalFilename) {
        log.info("Deleting image with {} filename", originalFilename);
        String filename = adaptFilename(originalFilename);
        storageService.deleteObject(filename, StoredType.IMG);
    }

    @Override
    public void deleteImgDir() {
        storageService.deleteStorageByType(StoredType.IMG);
    }

    private String adaptFilename(String originalFilename) {
        return originalFilename.replace(' ', '_');
    }
}

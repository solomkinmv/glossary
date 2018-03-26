package io.github.solomkinmv.glossary.image.service;

import java.io.InputStream;
import java.util.List;

/**
 * Describes all operations with images.
 */
public interface ImageService {
    int PAGE_SIZE = 10;

    /**
     * Returns list of the images by the specified {@code tags}.
     *
     * @param tags tags for the images
     * @return list of image urls
     */
    List<String> search(String[] tags);

    /**
     * Stores image {@code inputStream} to the external storage.
     *
     * @param inputStream      input stream for the image
     * @param originalFilename file name of the image
     * @return url for the stored image
     */
    String store(InputStream inputStream, String originalFilename);

    /**
     * Deletes uploaded image by {@code originalFilename}.
     *
     * @param originalFilename the name of the image
     */
    void deleteImg(String originalFilename);

    /**
     * Removes image upload directory.
     */
    void deleteStorage();
}

package io.github.solomkinmv.glossary.service.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import io.github.solomkinmv.glossary.service.exception.ImageSearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ImageSearchImpl implements ImageSearch {
    private static final int PAGE_SIZE = 10;

    private final Flickr flickr;

    @Autowired
    public ImageSearchImpl(Flickr flickr) {
        this.flickr = flickr;
    }

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
}

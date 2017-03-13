package io.github.solomkinmv.glossary.service.flickr;

import java.util.List;

public interface ImageSearch {

    List<String> search(String[] tags);
}

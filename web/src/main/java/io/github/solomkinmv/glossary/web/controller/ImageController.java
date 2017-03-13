package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.flickr.ImageSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for operations with images.
 */
@RestController
@RequestMapping("/api/images")
@Slf4j
public class ImageController {
    private final ImageSearch imageSearch;

    @Autowired
    public ImageController(ImageSearch imageSearch) {
        this.imageSearch = imageSearch;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Resources<String> search(@RequestParam("query") String query) {
        log.info("Searching for images by following query: {}", query);
        return new Resources<>(imageSearch.search(query.split(",")));
    }
}

package io.github.solomkinmv.glossary.service.search;

import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class SearchResult {
    List<Record> records;

    @Value
    static class Record {
        String text;
        Set<String> translations;
        Set<String> images;
        String sound;
    }
}

package io.github.solomkinmv.glossary.service.search;

import lombok.Value;

import java.util.List;

@Value
public class SearchResult {
    List<Record> records;

    @Value
    static class Record {
        String text;
        List<String> translations;
        List<String> images;
        String sound;
    }
}

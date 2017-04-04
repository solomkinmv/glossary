package io.github.solomkinmv.glossary.service.search;

public interface SearchService {
    int SEARCH_LIMIT = 5;

    SearchResult executeSearch(String text);
}

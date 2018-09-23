package io.github.solomkinmv.glossary.storage.filename;

import org.springframework.stereotype.Service;

@Service
public class FilenameAdapter {

    public String adapt(String originalFilename) {
        return removeIllegalCharactersExceptDotBeforeFileExtension(originalFilename);
    }

    private String removeIllegalCharacters(String filename) {
        return filename.replaceAll("\\s+", "-")
                       .replaceAll("[^a-zA-Z\\d\\-]", "");
    }

    private String removeIllegalCharactersExceptDotBeforeFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');

        return removeIllegalCharacters(filename.substring(0, dotIndex)) + filename.substring(dotIndex);
    }
}

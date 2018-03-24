package io.github.solomkinmv.glossary.storage.filename;

import org.springframework.stereotype.Service;

@Service
public class FilenameAdapter {

    public String adapt(String originalFilename) {
        return insertIdBeforeExtension(
                removeIllegalCharacters(originalFilename));
    }

    private String removeIllegalCharacters(String filename) {
        return filename.replace(" ", "")
                       .replace("/", "")
                       .replace("\\", "");
    }

    private String insertIdBeforeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');

        return filename.substring(0, dotIndex) + filename.substring(dotIndex);
    }
}

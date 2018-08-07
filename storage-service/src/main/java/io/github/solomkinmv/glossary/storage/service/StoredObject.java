package io.github.solomkinmv.glossary.storage.service;

import lombok.Value;

@Value
public class StoredObject {
    StoredType type;
    String url;
    String filename;
}

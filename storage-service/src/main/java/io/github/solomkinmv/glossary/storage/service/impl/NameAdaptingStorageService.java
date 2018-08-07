package io.github.solomkinmv.glossary.storage.service.impl;

import io.github.solomkinmv.glossary.storage.filename.FilenameAdapter;
import io.github.solomkinmv.glossary.storage.service.StorageService;
import io.github.solomkinmv.glossary.storage.service.StoredObject;
import io.github.solomkinmv.glossary.storage.service.StoredType;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public abstract class NameAdaptingStorageService implements StorageService {

    private final FilenameAdapter filenameAdapter;

    @Override
    public final StoredObject store(InputStream inputStream, String filename, StoredType type) {
        String adaptedFileName = filenameAdapter.adapt(filename);
        String url = storeWithoutAdaptingFilenames(inputStream, adaptedFileName, type);
        return new StoredObject(type, url, adaptedFileName);
    }

    protected abstract String storeWithoutAdaptingFilenames(InputStream inputStream, String filename, StoredType type);

}

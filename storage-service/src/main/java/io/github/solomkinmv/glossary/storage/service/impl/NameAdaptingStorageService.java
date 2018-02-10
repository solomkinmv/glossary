package io.github.solomkinmv.glossary.storage.service.impl;

import io.github.solomkinmv.glossary.storage.filename.FilenameAdapter;
import io.github.solomkinmv.glossary.storage.service.StorageService;
import io.github.solomkinmv.glossary.storage.service.StoredType;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public abstract class NameAdaptingStorageService implements StorageService {

    private final FilenameAdapter filenameAdapter;

    @Override
    public final String store(InputStream inputStream, String filename, StoredType type) {
        return storeWithoutAdaptingFilenames(inputStream,
                                             filenameAdapter.adapt(filename),
                                             type);
    }

    protected abstract String storeWithoutAdaptingFilenames(InputStream inputStream, String filename, StoredType type);

}

package io.github.solomkinmv.glossary.service.storage;

import java.io.InputStream;

/**
 * Describes operations for storing objects.
 * Uses {@link StoredType} to distinguish different object types.
 */
public interface StorageService {

    /**
     * Stores object of specified {@code type}.
     *
     * @param inputStream the input stream for the object
     * @param filename    the object's filename
     * @param type        the object's type
     * @return URL to the object
     */
    String store(InputStream inputStream, String filename, StoredType type);

    /**
     * Deletes object of specified {@code type} by the filename.
     *
     * @param filename the object's filename
     * @param type     the object's type
     */
    void deleteObject(String filename, StoredType type);

    /**
     * Deletes storage for all objects of the specified {@code type}.
     *
     * @param type the type of objects to be deleted
     */
    void deleteStorageByType(StoredType type);
}

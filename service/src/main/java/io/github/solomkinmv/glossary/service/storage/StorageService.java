package io.github.solomkinmv.glossary.service.storage;

import java.io.InputStream;
import java.util.Optional;

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

    /**
     * Returns object by specified {@code filename} and {@code type}.
     *
     * @param filename the object's filename
     * @param type     the object's type
     * @return an optional URL to the object
     */
    Optional<String> getObject(String filename, StoredType type);
}

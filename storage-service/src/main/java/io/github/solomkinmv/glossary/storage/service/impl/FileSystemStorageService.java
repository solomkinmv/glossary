package io.github.solomkinmv.glossary.storage.service.impl;

import io.github.solomkinmv.glossary.storage.exception.ObjectExistException;
import io.github.solomkinmv.glossary.storage.exception.StorageException;
import io.github.solomkinmv.glossary.storage.filename.FilenameAdapter;
import io.github.solomkinmv.glossary.storage.properties.StorageProperties;
import io.github.solomkinmv.glossary.storage.service.StorageService;
import io.github.solomkinmv.glossary.storage.service.StoredType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

/**
 * Implementation of the {@link StorageService} to store in the FileSystem.
 */
@Component
@Slf4j
@Profile("!s3")
public class FileSystemStorageService extends NameAdaptingStorageService {
    private static final int HTTP_PREFIX_LENGTH = "http://".length();
    private final HashMap<StoredType, Path> storedTypeDirMapping;
    private final HashMap<StoredType, String> storedTypeUrlPrefixMapping;
    private final String contextPath;

    @Autowired
    public FileSystemStorageService(StorageProperties storageProperties,
                                    FilenameAdapter filenameAdapter,
                                    @Value("${server.servlet.context-path:}") String contextPath) {
        super(filenameAdapter);
        this.contextPath = contextPath;

        log.debug("Build StoredType to dir mapping");
        storedTypeDirMapping = new HashMap<>();
        storedTypeDirMapping.put(StoredType.IMG, Paths.get(storageProperties.getImgUploadDir()));
        storedTypeDirMapping.put(StoredType.SOUND, Paths.get(storageProperties.getSoundUploadDir()));
        Assert.isTrue(storedTypeDirMapping.size() == StoredType.values().length,
                      "Stored type dir mapping should cover all StoredType enum constants");

        log.debug("Build StoredType to url prefix mapping");
        storedTypeUrlPrefixMapping = new HashMap<>();
        storedTypeUrlPrefixMapping.put(StoredType.IMG, storageProperties.getImgUrlPrefix());
        storedTypeUrlPrefixMapping.put(StoredType.SOUND, storageProperties.getSoundUrlPrefix());
        Assert.isTrue(storedTypeUrlPrefixMapping.size() == StoredType.values().length,
                      "Stored type url prefix mapping should cover all StoredType enum constants");

    }

    /**
     * Stores object's {@code inputStream} to the file system.
     *
     * @param inputStream input stream for the object
     * @param filename    file name of the object
     * @return url for the stored object
     */
    @Override
    protected String storeWithoutAdaptingFilenames(InputStream inputStream, String filename, StoredType type) {
        Path uploadDir = storedTypeDirMapping.get(type);
        Path objectPath = uploadDir.resolve(filename);

        if (Files.exists(objectPath)) {
            log.error("Object already exist: {}", filename);
            throw new ObjectExistException("Object file name: " + filename);
        }

        ensureCopyingDirectoryExist(uploadDir);

        try {
            Files.copy(inputStream, objectPath);
        } catch (IOException e) {
            String msg = "Can't store an object " + filename + " to " + uploadDir.toAbsolutePath();
            log.error(msg, e);
            throw new StorageException(msg, e);
        }

        return buildPath(filename, type);

    }

    @Override
    public void deleteObject(String filename, StoredType type) {
        log.info("Deleting {} of type {}", filename, type);
        Path uploadDir = storedTypeDirMapping.get(type);
        Path objectPath = uploadDir.resolve(filename);

        try {
            Files.deleteIfExists(objectPath);
        } catch (IOException e) {
            log.warn("Can't remove image " + objectPath);
        }
    }

    @Override
    public void deleteStorageByType(StoredType type) {
        log.info("Deleting directory for {} type", type);
        Path dir = storedTypeDirMapping.get(type);
        FileSystemUtils.deleteRecursively(dir.toFile());
    }

    @Override
    public Optional<String> getObject(String filename, StoredType type) {
        Path uploadDir = storedTypeDirMapping.get(type);
        Path objectPath = uploadDir.resolve(filename);

        if (!Files.exists(objectPath)) {
            return Optional.empty();
        }

        return Optional.of(buildPath(filename, type));
    }

    private void ensureCopyingDirectoryExist(Path dirPath) {
        log.debug("Ensuring that there is directory {} to store object", dirPath);
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
        } catch (IOException e) {
            String msg = "Can't create directory " + dirPath;
            log.error(msg, e);
            throw new StorageException(msg, e);
        }
    }

    private String buildPath(String objectName, StoredType type) {
        String requestUrl = extractRequestUrl();
        String urlPrefix = storedTypeUrlPrefixMapping.get(type);
        return requestUrl + urlPrefix + "/" + objectName;
    }

    private String extractRequestUrl() {
        String fullUrl = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRequestURL()
                .toString();

        // dirty hack to dynamically retrieve current host and port
        int colonIndex = fullUrl.indexOf(':', HTTP_PREFIX_LENGTH);
        int lastSlashIndex = fullUrl.indexOf('/', colonIndex);
        return fullUrl.substring(0, lastSlashIndex) + contextPath;
    }
}

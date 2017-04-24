package io.github.solomkinmv.glossary.service.storage.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import io.github.solomkinmv.glossary.service.exception.StorageException;
import io.github.solomkinmv.glossary.service.storage.StorageProperties;
import io.github.solomkinmv.glossary.service.storage.StorageService;
import io.github.solomkinmv.glossary.service.storage.StoredType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;

/**
 * Implementation of the {@link StorageService} to store in the S3.
 */
@Component
@Slf4j
@Profile("s3")
public class S3StorageService implements StorageService {
    private final AmazonS3 amazonS3;
    private final HashMap<StoredType, String> storedTypeBuckerMapping;

    @Autowired
    public S3StorageService(StorageProperties storageProperties, AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        log.debug("Build StoredType to dir mapping");
        storedTypeBuckerMapping = new HashMap<>();
        storedTypeBuckerMapping.put(StoredType.IMG, storageProperties.getImageS3BucketName());
        storedTypeBuckerMapping.put(StoredType.SOUND, storageProperties.getPronunciationS3BucketName());
        Assert.isTrue(storedTypeBuckerMapping.size() == StoredType.values().length,
                      "Stored type dir mapping should cover all StoredType enum constants");
    }

    @Override
    public String store(InputStream inputStream, String filename, StoredType type) {
        String bucketName = storedTypeBuckerMapping.get(type);
        if (amazonS3.doesObjectExist(bucketName, filename)) {
            log.error("Object already exist: {}", filename);
            filename = addIndexToFilename(filename, type);
        }

        File tmpFile = streamToTmpFile(inputStream, filename);
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, filename, tmpFile)
                                       .withCannedAcl(CannedAccessControlList.PublicRead));
            boolean deleteResult = tmpFile.delete();
            log.debug("Deleted tmp file: {}", deleteResult);
        } catch (SdkClientException e) {
            String msg = "Can't store an object " + filename;
            log.error(msg, e);
            throw new StorageException(msg, e);
        }
        String url = amazonS3.getUrl(bucketName, filename).toString();
        log.debug("Stored object url: " + url);
        return url;
    }

    @Override
    public void deleteObject(String filename, StoredType type) {
        String bucketName = storedTypeBuckerMapping.get(type);
        amazonS3.deleteObject(bucketName, filename);
    }

    @Override
    public void deleteStorageByType(StoredType type) {
        String bucketName = storedTypeBuckerMapping.get(type);
        amazonS3.listObjects(bucketName)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .forEach(key -> amazonS3.deleteObject(bucketName, key));
    }

    @Override
    public Optional<String> getObject(String filename, StoredType type) {
        String bucketName = storedTypeBuckerMapping.get(type);
        if (!amazonS3.doesObjectExist(bucketName, filename)) {
            return Optional.empty();
        }

        String url = amazonS3.getUrl(bucketName, filename).toString();
        return Optional.of(url);
    }

    private String addIndexToFilename(String filename, StoredType type) {
        int dotIndex = filename.lastIndexOf('.');
        String name = filename.substring(0, dotIndex);
        String bucketName = storedTypeBuckerMapping.get(type);
        int numberOfObjects = amazonS3.listObjects(bucketName, name).getObjectSummaries().size();
        return name + "-" + numberOfObjects + filename.substring(dotIndex);
    }

    private File streamToTmpFile(InputStream inputStream, String filename) {
        final File tempFile = createTempFile(filename);
        writeToFile(inputStream, tempFile);
        return tempFile;
    }

    private void writeToFile(InputStream inputStream, File tempFile) {
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, out);
        } catch (IOException e) {
            String msg = "Can't write input stream to temp file";
            log.error(msg, e);
            throw new StorageException(msg, e);
        }
    }

    private File createTempFile(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        String suffix = filename.substring(dotIndex);

        final File tempFile;
        try {
            tempFile = File.createTempFile("s3-service-stored-file", suffix);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            String msg = "Can't create temp file";
            log.error(msg, e);
            throw new StorageException(msg, e);
        }
        return tempFile;
    }
}

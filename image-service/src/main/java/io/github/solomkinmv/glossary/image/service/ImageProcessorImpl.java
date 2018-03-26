package io.github.solomkinmv.glossary.image.service;

import io.github.solomkinmv.glossary.image.exception.ImageProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
class ImageProcessorImpl implements ImageProcessor {

    @Override
    public InputStream fitByWidth(InputStream imageInputStream, int width) {
        log.info("Trying to fit image input stream by height {}", width);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            fitAndSendToOutputStream(imageInputStream, byteArrayOutputStream, width);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            String message = "Couldn't scale image";
            log.error(message, e);
            throw new ImageProcessingException(message, e);
        }
    }

    private void fitAndSendToOutputStream(InputStream inputStream, OutputStream outputStream, int width) throws IOException {
        try {
            Thumbnails.of(inputStream)
                      .width(width)
                      .toOutputStream(outputStream);
        } catch (UnsupportedFormatException e) {
            String msg = "Input stream is not an image";
            log.error(msg, e);
            throw new ImageProcessingException(msg, e);
        }
    }
}

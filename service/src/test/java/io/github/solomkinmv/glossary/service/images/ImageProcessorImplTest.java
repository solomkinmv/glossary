package io.github.solomkinmv.glossary.service.images;

import io.github.solomkinmv.glossary.service.exception.ImageProcessingException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ImageProcessorImplTest {
    private final ImageProcessor imageProcessor = new ImageProcessorImpl();

    @Test(expected = ImageProcessingException.class)
    public void throwsExceptionIfInputStreamIsNotImage() throws IOException {
        InputStream inputStream = getClass().getResource("/images/not_image.txt").openStream();

        imageProcessor.fitByWidth(inputStream, 300);
    }

    @Test
    public void resizesBiggerImage() throws IOException {
        int fixedWidth = 250;
        InputStream inputStream = getClass().getResource("/images/wide.png").openStream();

        InputStream processedInputStream = imageProcessor.fitByWidth(inputStream, fixedWidth);
        int width = ImageIO.read(processedInputStream).getWidth();

        assertEquals(fixedWidth, width);
    }

    @Test
    public void resizesSmallerImage() throws IOException {
        int fixedWidth = 400;
        InputStream inputStream = getClass().getResource("/images/wide.png").openStream();

        InputStream processedInputStream = imageProcessor.fitByWidth(inputStream, fixedWidth);
        int width = ImageIO.read(processedInputStream).getWidth();

        assertEquals(fixedWidth, width);
    }
}
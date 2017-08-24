package io.github.solomkinmv.glossary.service.images;

import java.io.InputStream;

public interface ImageProcessor {

    InputStream fitByWidth(InputStream imageInputStream, int width);
}

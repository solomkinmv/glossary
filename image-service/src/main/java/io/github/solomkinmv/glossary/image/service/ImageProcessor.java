package io.github.solomkinmv.glossary.image.service;

import java.io.InputStream;

public interface ImageProcessor {

    InputStream fitByWidth(InputStream imageInputStream, int width);
}

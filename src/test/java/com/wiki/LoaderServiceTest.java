package com.wiki;

import com.wiki.domain.Wiki;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class LoaderServiceTest {

    private LoaderService loaderService;

    @BeforeEach
    void setUp() {
        loaderService = new LoaderService();
    }

    @Test
    void load() throws URISyntaxException, IOException {
        Wiki wiki = loaderService.load(getFileFromResource("wiki"));
        System.out.printf("wiki");
    }

    private File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
}
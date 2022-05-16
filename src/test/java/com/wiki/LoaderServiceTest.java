package com.wiki;

import com.wiki.domain.Location;
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

        assertNotNull(wiki.getContainer());
        assertEquals(3, wiki.getFiles().size());
        assertEquals("wiki", wiki.getContainer().getName());
        assertEquals(1, wiki.getContainer().getContainers().size());
        assertEquals(1, wiki.getContainer().getDocuments().size());
        assertEquals(Location.of("/wiki"), wiki.getContainer().getLocation());

        assertEquals("Programowanie.md", wiki.getContainer().getDocuments().get(0).getName());
        assertEquals(3, wiki.getContainer().getDocuments().get(0).getContents().size());
        assertNotNull(wiki.getContainer().getDocuments().get(0).getContent());
        assertNotNull(wiki.getContainer().getDocuments().get(0).getLocation());
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
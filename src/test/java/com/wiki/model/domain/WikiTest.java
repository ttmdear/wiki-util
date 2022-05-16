package com.wiki.model.domain;

import com.wiki.model.service.LoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.wiki.util.FileUtil.getFileFromResource;
import static org.junit.jupiter.api.Assertions.*;

class WikiTest {

    private LoaderService loaderService;

    @BeforeEach
    void setUp() {
        loaderService = new LoaderService();
    }

    @Test
    void getIndexEntries() throws URISyntaxException, IOException {
        Wiki wiki = loaderService.load(getFileFromResource("wiki-index"));

        List<IndexEntry> indexEntries = wiki.getIndexEntries();

        System.out.printf("indexEntries", indexEntries);
    }
}
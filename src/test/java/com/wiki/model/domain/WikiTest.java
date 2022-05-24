package com.wiki.model.domain;

import com.wiki.model.service.LoadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.wiki.util.FileUtil.getFileFromResource;
import static org.junit.jupiter.api.Assertions.*;

class WikiTest {

    private LoadService loadService;

    @BeforeEach
    void setUp() {
        loadService = new LoadService(new ResolvePathService());
    }

    @Test
    void getIndexEntries() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki-index"));
        assertEquals(8, wiki.getIndexEntries().size());
    }

    @Test
    void search() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki-java"));
        assertNotNull(wiki.search("java-releases-connect-ssh"));
        assertNull(wiki.search("connect-ssh"));
    }

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        @Override
        public String resolveWikiPath() {
            return super.resolveWikiPath();
        }
    }
}
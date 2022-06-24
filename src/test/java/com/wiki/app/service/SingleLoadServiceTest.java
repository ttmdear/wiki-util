package com.wiki.app.service;

import com.wiki.model.domain.Location;
import com.wiki.model.domain.Wiki;
import com.wiki.app.service.impl.SingleLoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.wiki.util.FileUtil.getFileFromResource;
import static org.junit.jupiter.api.Assertions.*;

class SingleLoadServiceTest {

    SingleLoadService loadService;

    @BeforeEach
    void setUp() {
        loadService = new SingleLoadService(new ResolvePathService(), loadDocumentService);
    }

    @Test
    void loadTest() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki-test"));
        System.out.printf(wiki.search("java-releases"));
    }

    @Test
    void load() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki"));

        assertNotNull(wiki.getContainer());
        assertEquals(3, wiki.getFiles().size());
        assertEquals("wiki", wiki.getContainer().getName());
        assertEquals(1, wiki.getContainer().getDocuments().size());
        assertEquals(Location.of(""), wiki.getContainer().getLocation());

        assertEquals("Programowanie.md", wiki.getContainer().getDocuments().get(0).getName());
        assertEquals(3, wiki.getContainer().getDocuments().get(0).getContents().size());
        assertNotNull(wiki.getContainer().getDocuments().get(0).getContent());
        assertNotNull(wiki.getContainer().getDocuments().get(0).getLocation());
    }

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        @Override
        public String resolveWikiPath() {
            return super.resolveWikiPath();
        }
    }
}
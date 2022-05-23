package com.wiki;

import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.Location;
import com.wiki.model.domain.Wiki;
import com.wiki.model.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.wiki.util.FileUtil.getFileFromResource;
import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    private LoadService loadService;

    @BeforeEach
    void setUp() {
        loadService = new LoadService(new ResolvePathService());
    }

    @Test
    void load() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki"));

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

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        @Override
        public String resolveWikiPath() {
            return super.resolveWikiPath();
        }
    }
}
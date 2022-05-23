package com.wiki.model.domain;

import com.wiki.model.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.wiki.util.FileUtil.getFileFromResource;

class WikiTest {

    private LoadService loadService;

    @BeforeEach
    void setUp() {
        loadService = new LoadService(new ResolvePathService());
    }

    @Test
    void getIndexEntries() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki-index"));

        List<IndexEntry> indexEntries = wiki.getIndexEntries();

        System.out.printf("indexEntries", indexEntries);
    }

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        @Override
        public String resolveWikiPath() {
            return super.resolveWikiPath();
        }
    }
}
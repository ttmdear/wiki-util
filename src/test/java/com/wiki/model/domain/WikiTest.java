package com.wiki.model.domain;

import com.wiki.model.service.LoadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    void search() throws URISyntaxException, IOException {
        Wiki wiki = loadService.load(getFileFromResource("wiki-java"));
        assertNotNull(wiki.search("java-releases-connect-ssh"));
        assertNull(wiki.search("connect-ssh"));
    }

    @Test
    void searchByIndex() {
        List<Content> contents = Arrays.asList(
            new Content(ID.nextId(), new Head((short) 1, "Head 1"), Index.of("lvl1"), "content 1", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 2, "Head 2"), Index.of("lvl2"), "content 2", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 2, "Head 2b"), Index.of("lvl2b"), "content 3", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 5, "Head 5"), Index.of("lvl5"), "content 4", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 6, "Head 6b"), Index.of("lvl6b"), "content 5", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 6, "Head 6"), Index.of("lvl6"), "content 6", new ArrayList<>(), new ArrayList<>())
        );

        Document document = new Document(ID.nextId(), "Test", Content.createEmpty(ID.nextId()), contents, Location.of("/"));
        Container container = new Container(ID.nextId(), "test", Collections.emptyList(), Arrays.asList(document), Location.of("/"));

        Wiki wiki = new Wiki(container, Collections.emptyList());

        assertEquals("content 6", wiki.search("lvl1-lvl2b-lvl5-lvl6"));
    }

    private static class ResolvePathService extends com.wiki.app.service.ResolvePathService {
        @Override
        public String resolveWikiPath() {
            return super.resolveWikiPath();
        }
    }
}
package com.wiki.model.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void getContentIndexCaseA() {
        List<Content> contents = Arrays.asList(
            new Content(ID.nextId(), new Head((short) 1, "Head 1"), Index.of("lvl1"), "content 1", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 2, "Head 2"), Index.of("lvl2"), "content 2", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 3, "Head 3"), Index.of("lvl3"), "content 3", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 4, "Head 4"), Index.of("lvl4"), "content 3", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 5, "Head 5"), Index.of("lvl5"), "content 4", new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 6, "Head 6"), Index.of("lvl6"), "content 5", new ArrayList<>(), new ArrayList<>())
        );

        Document document = new Document(ID.nextId(), "Test", Content.createEmpty(ID.nextId(), Index.of("java")), contents, Location.of("/"));
        assertEquals(document.getContentIndex(contents.get(3)), Index.of("java-lvl1-lvl2-lvl3-lvl4"));
    }

    @Test
    void getContentIndexCaseB() {
        List<Content> contents = Arrays.asList(
            new Content(ID.nextId(), new Head((short) 1, "Head 1"), Index.of("lvl1"), null, new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 2, "Head 2"), Index.of("lvl2"), null, new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 2, "Head 2b"), Index.of("lvl2b"), null, new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 5, "Head 5"), Index.of("lvl5"), null, new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 6, "Head 6b"), Index.of("lvl6b"), null, new ArrayList<>(), new ArrayList<>()),
            new Content(ID.nextId(), new Head((short) 6, "Head 6"), Index.of("lvl6"), null, new ArrayList<>(), new ArrayList<>())
        );

        Document document = new Document(ID.nextId(), "Test", Content.createEmpty(ID.nextId()), contents, Location.of("/"));

        assertEquals(Index.of("lvl1-lvl2b-lvl5-lvl6"), document.getContentIndex(contents.get(contents.size() - 1)));
    }
}
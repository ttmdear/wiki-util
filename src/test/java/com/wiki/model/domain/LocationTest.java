package com.wiki.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void isPointMarkdown() {
        assertTrue(Location.of("/Programming/AVR/AVR.md").isPointMarkdown());
        assertFalse(Location.of("/Programming/AVR").isPointMarkdown());
    }

    @Test
    void getFileParentPath() {
        assertEquals("", Location.of("/AVR").getFileParentPath());
        assertEquals("/Programming/AVR", Location.of("/Programming/AVR/GAME").getFileParentPath());
    }

    @Test
    void getName() {
        assertEquals("test.md", Location.of("/AVR/Programming/test.md").getName());
    }
}
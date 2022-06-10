package com.wiki.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void isFile() {
        assertFalse(Path.of("http://o2.pl").isFile());
        assertFalse(Path.of("https://o2.pl").isFile());
        assertFalse(Path.of("sftp://o2.pl").isFile());

        assertTrue(Path.of("/wiki/it").isFile());
    }
}
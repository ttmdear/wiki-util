package com.wiki.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    @Test
    void isFile() {
        assertFalse(Link.of("http://o2.pl").isFile());
        assertFalse(Link.of("https://o2.pl").isFile());
        assertFalse(Link.of("sftp://o2.pl").isFile());

        assertTrue(Link.of("/wiki/it").isFile());
    }
}
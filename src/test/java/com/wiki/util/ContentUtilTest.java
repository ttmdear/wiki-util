package com.wiki.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContentUtilTest {

    @Test
    void appendLineWithEmpty() {
        StringBuilder stringBuilder = new StringBuilder();

        ContentUtil.appendLine(stringBuilder, "Hello", " ", "world", "!");

        assertEquals("Hello world!", stringBuilder.toString());
    }

    @Test
    void appendLineWithNonEmpty() {
        StringBuilder stringBuilder = new StringBuilder();

        ContentUtil.appendLine(stringBuilder, "Hello", " ", "world", "!");
        ContentUtil.appendLine(stringBuilder, "I'm", " ", "Paweł");

        assertEquals("Hello world!\nI'm Paweł", stringBuilder.toString());
    }
}
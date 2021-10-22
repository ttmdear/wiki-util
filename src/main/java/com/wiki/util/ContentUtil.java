package com.wiki.util;

public class ContentUtil {
    public static void appendLine(StringBuilder stringBuilder, String... content) {
        stringBuilder.append("\n");

        for (String s : content) {
            stringBuilder.append(s);
        }
    }
}

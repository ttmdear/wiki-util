package com.wiki.util;

public class ContentUtil {
    public static void appendLine(StringBuilder stringBuilder, String... content) {
        for (String s : content) {
            stringBuilder.append(s);
        }

        stringBuilder.append("\n");
    }
}

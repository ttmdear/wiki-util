package com.wiki.util;

public class ContentUtil {
    public static void appendLine(StringBuilder stringBuilder, String... content) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("\n");
        }

        for (String s : content) {
            stringBuilder.append(s);
        }
    }
}

package com.wiki.util;

public class PathUtil {
    public static String convertToLinuxPatch(String path) {
        return path.replaceAll("\\\\", "/");
    }
}

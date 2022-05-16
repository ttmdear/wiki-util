package com.wiki.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtil {

    private FileUtil() {

    }

    public static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
}

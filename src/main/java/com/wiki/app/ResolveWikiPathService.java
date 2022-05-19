package com.wiki.app;

public class ResolveWikiPathService {
    public String resolveWikiPath() {
        String path = System.getenv("WIKI_PATH");

        if (path == null) {
            throw new RuntimeException("There is no WIKI_PATH set. Please set env variable.");
        }

        return path;
    }
}
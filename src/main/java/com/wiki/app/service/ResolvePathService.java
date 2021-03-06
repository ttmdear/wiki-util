package com.wiki.app.service;

public class ResolvePathService {
    public String resolveWikiPath() {
        String path = System.getenv("WIKI_PATH");

        if (path == null) {
            path = System.getProperty("WIKI_PATH");
        }

        if (path == null) {
            throw new RuntimeException("There is no WIKI_PATH set. Please set env variable.");
        }

        return path;
    }

    public String resolveWikiCliPath() {
        return String.format("%s/%s", resolveWikiPath(), "run.wiki-cli.sh");
    }
}
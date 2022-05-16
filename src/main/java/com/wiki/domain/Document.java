package com.wiki.domain;

import java.util.List;

public class Document {
    private ID id;
    private String name;
    private Content content;
    private List<Content> contents;
    private Location location;

    public Document(ID id, String name, Content content, List<Content> contents, Location location) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.contents = contents;
        this.location = location;
    }
}

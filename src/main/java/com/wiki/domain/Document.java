package com.wiki.domain;

import java.util.List;

public class Document {
    private ID id;
    private String name;
    private Content content;
    private List<Content> contents;

    public Document(ID id, String name, Content content, List<Content> contents) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.contents = contents;
    }
}

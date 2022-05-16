package com.wiki.model.domain;

import java.util.Collections;
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

    public ID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public List<Content> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public Location getLocation() {
        return location;
    }

    public Index getIndex() {
        if (content != null) {
            return content.getIndex();
        }

        return null;
    }
}

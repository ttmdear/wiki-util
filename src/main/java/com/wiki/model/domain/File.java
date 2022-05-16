package com.wiki.model.domain;

public class File {
    private ID id;
    private String name;
    private Location path;

    public File(ID id, String name, Location path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}

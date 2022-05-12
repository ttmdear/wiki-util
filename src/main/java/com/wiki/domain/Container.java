package com.wiki.domain;

import java.util.List;

public class Container {
    private ID id;
    private String name;
    private List<Container> containers;
    private List<Document> documents;

    public Container(ID id, String name, List<Container> containers, List<Document> documents) {
        this.id = id;
        this.name = name;
        this.containers = containers;
        this.documents = documents;
    }
}

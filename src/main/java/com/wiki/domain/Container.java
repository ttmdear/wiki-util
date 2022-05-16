package com.wiki.domain;

import java.util.Collections;
import java.util.List;

public class Container {
    private ID id;
    private String name;
    private List<Container> containers;
    private List<Document> documents;
    private Location location;

    public Container(ID id, String name, List<Container> containers, List<Document> documents, Location location) {
        this.id = id;
        this.name = name;
        this.containers = containers;
        this.documents = documents;
        this.location = location;
    }

    public ID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Container> getContainers() {
        return Collections.unmodifiableList(containers);
    }

    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public Location getLocation() {
        return location;
    }
}

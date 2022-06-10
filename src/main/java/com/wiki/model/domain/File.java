package com.wiki.model.domain;

public class File {
    private ID id;
    private String name;
    private Location location;

    public File(ID id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}

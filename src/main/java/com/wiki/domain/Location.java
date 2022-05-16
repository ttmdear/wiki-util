package com.wiki.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class Location {
    private static final HashMap<String, Location> INSTANCES = new HashMap<>();

    String path;

    private Location(String path) {
        this.path = path;
    }

    public static Location of(String path) {
        return INSTANCES.computeIfAbsent(path, Location::new);
    }

    public Location locationTo(String name) {
        return Location.of(path + "/" + name);
    }
}

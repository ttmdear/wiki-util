package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class LinkRef {
    private static final HashMap<Path, LinkRef> INSTANCES = new HashMap<>();

    Path path;

    private LinkRef(Path path) {
        this.path = path;
    }

    public static LinkRef of(Path path) {
        return INSTANCES.computeIfAbsent(path, LinkRef::new);
    }

    public static LinkRef of(String link) {
        return INSTANCES.computeIfAbsent(Path.of(link), LinkRef::new);
    }
}

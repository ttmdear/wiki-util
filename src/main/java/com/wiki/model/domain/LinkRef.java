package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class LinkRef {
    private static final ConcurrentHashMap<Path, LinkRef> INSTANCES = new ConcurrentHashMap<>();

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

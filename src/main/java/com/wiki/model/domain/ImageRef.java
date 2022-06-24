package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class ImageRef {
    private static final ConcurrentHashMap<Path, ImageRef> INSTANCES = new ConcurrentHashMap<>();

    Path path;

    private ImageRef(Path path) {
        this.path = path;
    }

    public static ImageRef of(Path path) {
        return INSTANCES.computeIfAbsent(path, ImageRef::new);
    }

    public static ImageRef of(String link) {
        return INSTANCES.computeIfAbsent(Path.of(link), ImageRef::new);
    }
}

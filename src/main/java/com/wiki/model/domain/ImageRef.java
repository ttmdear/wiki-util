package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class ImageRef {
    private static final HashMap<Path, ImageRef> INSTANCES = new HashMap<>();

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

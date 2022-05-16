package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class ImageRef {
    private static final HashMap<String, ImageRef> INSTANCES = new HashMap<>();

    String link;

    private ImageRef(String link) {
        this.link = link;
    }

    public static ImageRef of(String link) {
        return INSTANCES.computeIfAbsent(link, ImageRef::new);
    }
}

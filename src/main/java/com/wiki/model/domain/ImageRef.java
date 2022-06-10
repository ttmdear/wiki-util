package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class ImageRef {
    private static final HashMap<Link, ImageRef> INSTANCES = new HashMap<>();

    Link link;

    private ImageRef(Link link) {
        this.link = link;
    }

    public static ImageRef of(Link link) {
        return INSTANCES.computeIfAbsent(link, ImageRef::new);
    }

    public static ImageRef of(String link) {
        return INSTANCES.computeIfAbsent(Link.of(link), ImageRef::new);
    }
}

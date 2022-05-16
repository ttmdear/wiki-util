package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class LinkRef {
    private static final HashMap<String, LinkRef> INSTANCES = new HashMap<>();

    String link;

    private LinkRef(String link) {
        this.link = link;
    }

    public static LinkRef of(String link) {
        return INSTANCES.computeIfAbsent(link, LinkRef::new);
    }
}

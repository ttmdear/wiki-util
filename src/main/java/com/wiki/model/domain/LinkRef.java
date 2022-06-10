package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class LinkRef {
    private static final HashMap<Link, LinkRef> INSTANCES = new HashMap<>();

    Link link;

    private LinkRef(Link link) {
        this.link = link;
    }

    public static LinkRef of(Link link) {
        return INSTANCES.computeIfAbsent(link, LinkRef::new);
    }

    public static LinkRef of(String link) {
        return INSTANCES.computeIfAbsent(Link.of(link), LinkRef::new);
    }
}

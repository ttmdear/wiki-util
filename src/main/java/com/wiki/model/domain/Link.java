package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class Link {
    private static final HashMap<String, Link> INSTANCES = new HashMap<>();

    String link;

    private Link(String link) {
        this.link = link;
    }

    public static Link of(String link) {
        return INSTANCES.computeIfAbsent(link, Link::new);
    }

    public boolean isFile() {
        if (link.contains("://")) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return link;
    }
}

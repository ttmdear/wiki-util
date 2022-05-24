package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class Index {
    private static final HashMap<String, Index> INSTANCES = new HashMap<>();

    String key;

    private Index(String key) {
        this.key = key;
    }

    public static Index of(String key) {
        return INSTANCES.computeIfAbsent(key, Index::new);
    }

    public static Index of(Index a, Index b) {
        return of(a.key + "-" + b.key);
    }
}

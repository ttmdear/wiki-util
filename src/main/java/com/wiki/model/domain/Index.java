package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class Index {
    private static final ConcurrentHashMap<String, Index> INSTANCES = new ConcurrentHashMap<>();

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

    @Override
    public String toString() {
        return key;
    }
}

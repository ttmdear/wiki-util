package com.wiki.model.domain;

import lombok.Value;

@Value
public class Head {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 6;

    Short level;
    String name;

    public Head(Short level, String name) {
        if (level != null && (level < MIN_LEVEL || level > MAX_LEVEL)) {
            throw new RuntimeException(String.format("Head level is out of range %s to %s", MIN_LEVEL, MAX_LEVEL));
        }

        this.level = level;
        this.name = name;
    }

    public static Head createEmpty() {
        return new Head(null, null);
    }
}

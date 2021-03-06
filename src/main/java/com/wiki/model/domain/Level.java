package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class Level {
    private static final ConcurrentHashMap<Short, Level> INSTANCES = new ConcurrentHashMap<>();
    public static final Level L0 = Level.of((short) 0);
    public static final Level L1 = Level.of((short) 1);
    public static final Level L2 = Level.of((short) 2);
    public static final Level L3 = Level.of((short) 3);
    public static final Level L4 = Level.of((short) 4);
    public static final Level L5 = Level.of((short) 5);
    public static final Level L6 = Level.of((short) 6);

    Short value;

    public static Level of(Short value) {
        return INSTANCES.computeIfAbsent(value, Level::new);
    }
}

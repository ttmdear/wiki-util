package com.wiki.model.domain;

import lombok.Value;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class ID {
    private static final ConcurrentHashMap<Integer, ID> INSTANCES = new ConcurrentHashMap<>();
    private static Integer SEQ = 0;
    Integer value;

    public static synchronized ID nextId() {
        ID id = new ID(SEQ++);

        INSTANCES.put(id.value, id);

        return id;
    }
}

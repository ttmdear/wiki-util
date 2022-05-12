package com.wiki.domain;

import lombok.Value;

import java.util.HashMap;

@Value
public class ID {
    private static final HashMap<Integer, ID> INSTANCES = new HashMap<>();
    private static Integer SEQ = 0;
    Integer value;

    public static synchronized ID nextId() {
        ID id = new ID(SEQ++);

        INSTANCES.put(id.value, id);

        return id;
    }
}

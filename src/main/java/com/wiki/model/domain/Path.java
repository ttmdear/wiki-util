package com.wiki.model.domain;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class Path {
    private static final ConcurrentHashMap<String, Path> INSTANCES = new ConcurrentHashMap<>();

    String value;

    private Path(String value) {
        this.value = value;
    }

    public static Path of(String value) {
        return INSTANCES.computeIfAbsent(value, Path::new);
    }

    public boolean isFile() {
        if (value.contains("://")) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return value;
    }

    public String getName() {
        if (value == null || value.isEmpty()) {
            return null;
        }

        String[] split = value.split("/");

        return split[split.length - 1];
    }

    public String getParentPath() {
        if (value == null || value.isEmpty()) {
            return null;
        }

        String[] split = value.split("/");
        split = Arrays.copyOfRange(split, 1, split.length - 1);

        if (split.length == 0) {
            return "";
        } else {
            return "/" + StringUtils.join(split, "/");
        }
    }
}

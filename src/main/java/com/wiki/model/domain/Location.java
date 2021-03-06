package com.wiki.model.domain;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Value
public class Location {
    private static final ConcurrentHashMap<Path, Location> INSTANCES = new ConcurrentHashMap<>();
    public static final Location ROOT = Location.of("");

    Path path;

    private Location(Path path) {
        this.path = path;
    }

    public static Location of(String path) {
        return INSTANCES.computeIfAbsent(Path.of(path), Location::new);
    }

    public Location locationTo(String name) {
        return Location.of(path + "/" + name);
    }

    public boolean isPointMarkdown() {
        if (path.getName().endsWith("md")) {
            return true;
        } else {
            return false;
        }
    }

    public String getFileParentPath() {
        return path.getParentPath();
    }

    public String getName() {
        return path.getName();
    }
}

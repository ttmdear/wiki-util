package com.wiki.model.domain;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;

@Value
public class Location {
    private static final HashMap<String, Location> INSTANCES = new HashMap<>();

    String link;

    private Location(String link) {
        this.link = link;
    }

    public static Location of(String path) {
        return INSTANCES.computeIfAbsent(path, Location::new);
    }

    public Location locationTo(String name) {
        return Location.of(link + "/" + name);
    }

    public boolean isPointMarkdown() {
        if (link == null || link.isEmpty()) {
            return false;
        }

        String[] split = link.split("/");
        split = split[split.length - 1].split("\\.");

        if (split[split.length - 1].equals("md")) {
            return true;
        } else {
            return false;
        }
    }

    public String getFileParentPath() {
        if (link == null || link.isEmpty()) {
            return null;
        }

        String[] split = link.split("/");
        split = Arrays.copyOfRange(split, 1, split.length - 1);

        if (split.length == 0) {
            return "";
        } else {
            return "/" + StringUtils.join(split, "/");
        }
    }
}

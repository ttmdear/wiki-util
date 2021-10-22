package com.wiki.wiki;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Node {
    public static final int NO_LEVEL = -1;

    private String id;
    private String name;
    private Type type;
    private Node parentNode;
    private int level;
    private boolean isNew;
    private String content;
    private List<Node> children = new ArrayList<>();
    private List<FileLink> fileLinkList = new ArrayList<>();

    public Node(String id, boolean isNew, String name, Type type, int level, Node parentNode) {
        this.id = id;
        this.isNew = isNew;
        this.name = name;
        this.type = type;
        this.parentNode = parentNode;
        this.level = level;
    }

    public boolean isDirectory() {
        return type != null && type.equals(Type.DIRECTORY);
    }

    public boolean isFile() {
        return type != null && type.equals(Type.FILE);
    }

    public boolean isContent() {
        return type != null && type.equals(Type.CONTENT);
    }

    public enum Type {
        DIRECTORY, FILE, CONTENT
    }
}

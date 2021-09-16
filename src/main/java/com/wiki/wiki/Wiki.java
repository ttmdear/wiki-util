package com.wiki.wiki;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wiki {

    private final Map<String, Node> nodeIdMap = new HashMap<>();

    @Getter
    private final List<String> fileList = new ArrayList<>();

    @Getter
    private Node rootNode;

    public Wiki() {
    }

    public void addFile(String file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File can not be null or empty.");
        }

        fileList.add(file);
    }

    public void addNode(Node node, Node parentNode) {
        if (parentNode == null) {
            if (rootNode != null) {
                throw new IllegalArgumentException("Root node is already defined.");
            }

            rootNode = node;
            nodeIdMap.put(node.getId(), node);

            return;
        }

        nodeIdMap.put(node.getId(), node);

        Node parentNodeFromIndex = nodeIdMap.get(parentNode.getId());

        if (parentNodeFromIndex == null) {
            throw new IllegalArgumentException(String.format("No parent node with id [%s].", parentNode.getId()));
        }

        parentNodeFromIndex.getChildren().add(node);
    }

    public List<Node> getAllNodeList() {
        return new ArrayList<>(nodeIdMap.values());
    }

    public Node getNodeById(String nodeId) {
        return nodeIdMap.get(nodeId);
    }

    public String getPath(String nodeId) {
        Node node = nodeIdMap.get(nodeId);

        if (node == null) return null;

        List<String> pathElement = new ArrayList<>();
        StringBuilder pathBuilder = new StringBuilder();

        while (node.getParentNode() != null) {
            pathElement.add(node.getName());
            pathElement.add("/");

            node = node.getParentNode();
        }

        int size = pathElement.size();
        for (int i = size - 1; i >= 0; i--) {
            pathBuilder.append(pathElement.get(i));
        }

        return pathBuilder.toString();
    }
}

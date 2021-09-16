package com.wiki.reorganize;

import com.wiki.bootstrap.Configuration;
import com.wiki.wiki.Node;
import com.wiki.wiki.Wiki;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.format;

public class ChangeFileWriterImpl implements ChangeFileWriter {
    private static final String[] LEVEL_INDENTATION = {
            Configuration.INDENTATION_CHAR,
            Configuration.INDENTATION_CHAR_2,
            Configuration.INDENTATION_CHAR_3,
            Configuration.INDENTATION_CHAR_4,
            Configuration.INDENTATION_CHAR_5,
            Configuration.INDENTATION_CHAR_6
    };

    @Override
    public void write(Wiki wiki, String wikiChangeFilePath) {
        StringBuilder changeFileBuilder = new StringBuilder();

        if (wiki.getRootNode() == null) {
            throw new IllegalArgumentException("Wiki structure should have root node.");
        }

        for (Node childrenNode : wiki.getRootNode().getChildren()) {
            writeNode(childrenNode, "", changeFileBuilder);
        }

        try {
            FileWriter fileWriter = new FileWriter(wikiChangeFilePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(changeFileBuilder.toString());

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNode(Node node, String ind, StringBuilder changeFileBuilder) {
        if (changeFileBuilder.length() != 0) {
            changeFileBuilder.append("\n");
        }

        changeFileBuilder.append(format("%s%s (%s %s)", ind, node.getName(), node.getId(), node.getType()));

        if (node.getChildren().isEmpty()) return;

        if (node.getType().equals(Node.Type.FILE)) {
            for (Node childNode : node.getChildren()) {
                changeFileBuilder.append(format("\n%s%s (%s %s)", ind + LEVEL_INDENTATION[childNode.getLevel()],
                        childNode.getName(), childNode.getId(), childNode.getType()));
            }
        } else {
            for (Node childNode : node.getChildren()) {
                writeNode(childNode, ind + Configuration.INDENTATION_CHAR, changeFileBuilder);
            }
        }
    }
}

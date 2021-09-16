package com.wiki.reorganize;

import com.google.common.base.CharMatcher;
import com.wiki.util.IDUtil;
import com.wiki.wiki.Node;
import com.wiki.wiki.Node.Type;
import com.wiki.wiki.Wiki;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class ChangeFileReaderImpl implements ChangeFileReader {
    private static final Pattern PATTERN_LINE = Pattern
            .compile("(.*) \\((.*) (DIRECTORY|FILE|CONTENT)\\)", Pattern.MULTILINE);
    private static final CharMatcher TAB_MATCHER = CharMatcher.is('\t');

    public ChangeFileReaderImpl() {
    }

    public static int resolveIndentationLevel(String line) {
        if (line == null || line.isEmpty()) {
            return 0;
        }

        return TAB_MATCHER.countIn(line);
    }

    private LineParsed parseLine(String line) {
        int level = resolveIndentationLevel(line);

        Matcher matcher = PATTERN_LINE.matcher(line);

        if (!matcher.find()) {
            throw new RuntimeException("Incorrect line format [" + line + "]");
        }

        String name = matcher.group(1).trim();
        String id = matcher.group(2);
        boolean isNew = false;
        Node.Type type = Node.Type.valueOf(matcher.group(3));

        if (id.equals("?")) {
            id = IDUtil.generateUUID();
            isNew = true;
        }

        return new LineParsed(id, isNew, name, type, level);
    }

    @Override
    public Wiki read(String wikiChangeFilePath) throws IOException {
        File changeFile = new File(wikiChangeFilePath);

        if (!changeFile.exists() || !changeFile.isFile()) {
            throw new IllegalArgumentException(
                    String.format("File [%s] is not correct change file.", wikiChangeFilePath));
        }

        File directoryFile = changeFile.getParentFile();

        Wiki wiki = new Wiki();

        Node rootNode = new Node(IDUtil.getNextIdString(), false, directoryFile.getName(), Type.DIRECTORY, -1, null);
        wiki.addNode(rootNode, null);

        InputStream inputStream = new FileInputStream(changeFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        HashMap<Integer, Node> levelNode = new HashMap<>();
        levelNode.put(-1, rootNode);

        String line;
        Node node = null;
        Node nodeParent;
        LineParsed parsed;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) continue;

            parsed = parseLine(line);

            if (parsed.type.equals(Type.DIRECTORY) || parsed.type.equals(Type.FILE)) {
                nodeParent = levelNode.get(parsed.level - 1);

                if (nodeParent == null) {
                    throw new RuntimeException(format("No parent node for line [%s].", line));
                } else if (!nodeParent.getType().equals(Type.DIRECTORY)) {
                    throw new RuntimeException(format("Incorrect parent [%s]. Should be directory.", line));
                }

                node = new Node(parsed.id, parsed.isNew, parsed.name, parsed.type, parsed.level, nodeParent);
                levelNode.put(parsed.level, node);

                wiki.addNode(node, nodeParent);
            } else if (parsed.type.equals(Type.CONTENT)) {
                if (node == null || !node.getType().equals(Type.FILE)) {
                    throw new RuntimeException(format("Parent of line [%s] should be file.", line));
                }

                Node contentNode = new Node(parsed.id, parsed.isNew, parsed.name, parsed.type,
                        parsed.level - node.getLevel() - 1, node);

                wiki.addNode(contentNode, node);
            }
        }

        return wiki;
    }

    @RequiredArgsConstructor
    private static class LineParsed {
        private final String id;
        private final boolean isNew;
        private final String name;
        private final Node.Type type;
        private final int level;
    }
}

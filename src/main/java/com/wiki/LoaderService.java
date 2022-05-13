package com.wiki;

import com.wiki.domain.*;
import com.wiki.util.MatchUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoaderService {
    private static final String INDEX_REGEX = "\\[\\/\\/\\]: \\\"(.*)\\\"";

    public Wiki load(File directory) throws IOException {
         return new Wiki(loadDirectory(directory));
    }

    private Container loadDirectory(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(String.format("File %s doesn't directory", directory.getPath()));
        }

        List<Container> containers = new ArrayList<>();
        List<Document> documents = new ArrayList<>();

        for (File file : prepareFiles(directory)) {
            if (file.isDirectory()) {
                containers.add(loadDirectory(file));
            } else if (isMarkdownFile(file)) {
                documents.add(loadDocument(file));
            }
        }

        return new Container(ID.nextId(), directory.getName(), containers, documents);
    }

    private Document loadDocument(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        StringBuilder contentBuilder = new StringBuilder();

        // 0 - the first part of file
        // 1 - the contents file part
        // 2 - code
        ID id = ID.nextId();
        String name = file.getName();
        Content content = null;
        List<Content> contents = new ArrayList<>();
        State state = State.BEGIN;
        State statePrevCode = null;
        Head head = null;
        Index index = null;

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("```")) {
                contentBuilder.append(line);

                if (state.isCode()) {
                    state = statePrevCode;
                } else {
                    statePrevCode = state;
                    state = State.CODE;
                }
            } else if (state.isCode()) {
                contentBuilder.append(line);
            } else if (isHeadLine(line)) {
                if (state.isBegin()) {
                    content = prepareContent(ID.nextId(), head, index, contentBuilder.toString());
                    state = State.CONTENT;
                } else if (state.isContent()) {
                    contents.add(prepareContent(ID.nextId(), head, index, contentBuilder.toString()));
                }

                head = resolveHeadLine(line);
                index = null;
                contentBuilder.delete(0, contentBuilder.length());
            } else if (isIndexLine(line)) {
                index = parseIndexLine(line);
                contentBuilder.append(line);
            } else {
                contentBuilder.append(line);
            }
        }

        if (contentBuilder.length() > 0) {
            if (state.isBegin()) {
                content = prepareContent(ID.nextId(), head, index, contentBuilder.toString());
            } else if (state.isContent() || state.isCode()) {
                contents.add(prepareContent(ID.nextId(), head, index, contentBuilder.toString()));
            }
        }

        reader.close();

        return new Document(id, name, content, contents);
    }

    private Content prepareContent(ID id, Head head, Index index, String content) {
        List<ImageRef> imageRefs = parseImageRef(content);
        List<LinkRef> linkRefs = parseLinkRef(content);

        return new Content(id, head, index, content, imageRefs, linkRefs);
    }

    private List<LinkRef> parseLinkRef(String content) {
        List<LinkRef> result = new ArrayList<>();

        parseLinkRefHTML(content, result);
        parseLinkRefMarkdown(content, result);

        return result;
    }

    private void parseLinkRefHTML(String content, List<LinkRef> result) {
        for (String link : MatchUtil.matchAllIn(content, "<a.*?href=\\\".*?\\\">.*?<\\/a>", "href=\\\"(.*?)\\\"")) {
            result.add(LinkRef.of(link));
        }
    }

    private void parseLinkRefMarkdown(String content, List<LinkRef> result) {
        for (String link : MatchUtil.matchAllIn(content, "[^!]\\[(.*?)\\]\\(.*?\\)", "\\((.*?)\\)")) {
            result.add(LinkRef.of(link));
        }
    }

    private List<ImageRef> parseImageRef(String content) {
        List<ImageRef> result = new ArrayList<>();

        parseImageRefHTML(content, result);
        parseImageRefMarkdown(content, result);

        return result;
    }

    private void parseImageRefHTML(String content, List<ImageRef> result) {
        for (String link : MatchUtil.matchAllIn(content, "<img.*?\\/>", "src=\\\"(.*?)\\\"")) {
            result.add(ImageRef.of(link));
        }
    }

    private void parseImageRefMarkdown(String content, List<ImageRef> result) {
        for (String link : MatchUtil.matchAllIn(content, "!\\[.*?]\\(.*?\\)", "\\((.*?)\\)")) {
            result.add(ImageRef.of(link));
        }
    }

    private Head resolveHeadLine(String line) {
        if (line.startsWith("# ")) return new Head((short) 1, line.substring(2));
        else if (line.startsWith("## ")) return new Head((short) 2, line.substring(3));
        else if (line.startsWith("### ")) return new Head((short) 3, line.substring(4));
        else if (line.startsWith("#### ")) return new Head((short) 4, line.substring(5));
        else if (line.startsWith("##### ")) return new Head((short) 5, line.substring(6));
        else if (line.startsWith("###### ")) return new Head((short) 6, line.substring(7));

        throw new IllegalArgumentException("It's not head line.");
    }

    private boolean isHeadLine(String line) {
        if (line.startsWith("# ")) return true;
        else if (line.startsWith("## ")) return true;
        else if (line.startsWith("### ")) return true;
        else if (line.startsWith("#### ")) return true;
        else if (line.startsWith("##### ")) return true;
        else if (line.startsWith("###### ")) return true;
        return false;
    }

    private boolean isIndexLine(String line) {
        if (line.startsWith("[//]:")) return true;
        else return false;
    }

    private Index parseIndexLine(String line) {
        return Index.of(MatchUtil.match(line, INDEX_REGEX));
    }

    private List<File> prepareFiles(File directory) {
        File[] files = directory.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        List<File> fileList = Arrays.asList(files);
        fileList.sort(Comparator.comparing(File::getName));

        return fileList;
    }

    private static boolean isMarkdownFile(File file) {
        if (file == null) {
            return false;
        }

        String[] splited = file.getName().split("\\.");

        if (splited.length == 0) {
            return false;
        }

        return splited[splited.length - 1].equals("md");
    }

    private enum State {
        BEGIN, CODE, CONTENT;

        boolean isBegin() {
            return this.equals(BEGIN);
        }

        boolean isCode() {
            return this.equals(CODE);
        }

        boolean isContent() {
            return this.equals(CONTENT);
        }
    }
}

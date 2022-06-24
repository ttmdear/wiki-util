package com.wiki.app.service;

import com.wiki.model.domain.Content;
import com.wiki.model.domain.Document;
import com.wiki.model.domain.Head;
import com.wiki.model.domain.ID;
import com.wiki.model.domain.ImageRef;
import com.wiki.model.domain.Index;
import com.wiki.model.domain.LinkRef;
import com.wiki.model.domain.Location;
import com.wiki.model.exceptions.LoadException;
import com.wiki.util.MatchUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoadDocumentService {
    private static final String INDEX_REGEX_A = "\\[\\/\\/\\]:#\\\"(.*)\\\"";
    private static final String INDEX_REGEX_B = "\\[\\/\\/\\]:\\\"(.*)\\\"";
    private static final String INDEX_REGEX_C = "\\[\\/\\/\\]:\\((.*?)\\)";

    public void load(Document document, File file) throws IOException {
        InputStream input = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        StringBuilder contentBuilder = new StringBuilder();

        // 0 - the first part of file
        // 1 - the contents file part
        // 2 - code
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
                append(contentBuilder, line);

                if (state.isCode()) {
                    state = statePrevCode;
                } else {
                    statePrevCode = state;
                    state = State.CODE;
                }
            } else if (state.isCode()) {
                append(contentBuilder, line);
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
                append(contentBuilder, line);
            } else if (isIndexLine(line)) {
                index = parseIndexLine(line);
                append(contentBuilder, line);
            } else {
                append(contentBuilder, line);
            }
        }

        if (contentBuilder.length() > 0) {
            if (state.isBegin()) {
                content = prepareContent(ID.nextId(), head, index, contentBuilder.toString());
            } else if (state.isContent() || state.isCode()) {
                contents.add(prepareContent(ID.nextId(), head, index, contentBuilder.toString()));
            }
        } else {
            if (state.isBegin()) {
                content = Content.createEmpty(ID.nextId());
            } else if (head != null) {
                contents.add(prepareContent(ID.nextId(), head, index, contentBuilder.toString()));
            } else {
                throw new LoadException("Strange case.");
            }
        }

        reader.close();

        document.load(name, content, contents);
    }

    private void append(StringBuilder contentBuilder, String line) {
        if (line == null || line.isEmpty()) {
            return;
        }

        if (contentBuilder.length() > 0) {
            contentBuilder.append("\n");
        }

        contentBuilder.append(line);
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
        else return line.startsWith("###### ");
    }

    private boolean isIndexLine(String line) {
        return line.startsWith("[//]:");
    }

    private Index parseIndexLine(String line) {
        line = line.replaceAll(" ", "");

        String key = MatchUtil.match(line, INDEX_REGEX_A);

        if (key == null) {
            key = MatchUtil.match(line, INDEX_REGEX_B);
        }

        if (key == null) {
            key = MatchUtil.match(line, INDEX_REGEX_C);
        }

        if (key == null) {
            throw new LoadException(String.format("Incorrect index in line [%s]", line));
        }

        return Index.of(key);
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

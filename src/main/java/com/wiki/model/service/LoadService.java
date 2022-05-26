package com.wiki.model.service;

import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.*;
import com.wiki.model.exceptions.LoadException;
import com.wiki.util.MatchUtil;

import javax.inject.Inject;
import java.io.*;
import java.io.File;
import java.util.*;

public class LoadService {
    private static final String INDEX_REGEX_A = "\\[\\/\\/\\]:#\\\"(.*)\\\"";
    private static final String INDEX_REGEX_B = "\\[\\/\\/\\]:\\\"(.*)\\\"";
    private static final String INDEX_REGEX_C = "\\[\\/\\/\\]:\\((.*?)\\)";

    private final ResolvePathService resolvePathService;

    @Inject
    public LoadService(ResolvePathService resolvePathService) {
        this.resolvePathService = resolvePathService;
    }

    public Wiki load() throws IOException {
        return load(new File(resolvePathService.resolveWikiPath()));
    }

    public Wiki load(File root) throws IOException {
        Location location = Location.of("/" + root.getName());

        return new Wiki(loadDirectory(root, location), loadFiles(root, location));
    }

    private List<com.wiki.model.domain.File> loadFiles(File directory, Location location) {
        List<File> files = prepareFiles(directory);

        if (files.isEmpty()) {
            return new ArrayList<>();
        }

        Optional<File> filesOpt = files.stream()
            .filter(file -> file.isDirectory() && file.getName().equals(".files"))
            .findFirst();

        if (!filesOpt.isPresent()) {
            return new ArrayList<>();
        }

        List<com.wiki.model.domain.File> result = new ArrayList<>();
        Location filesLoc = location.locationTo(".files");

        for (File fileFs : prepareFiles(filesOpt.get())) {
            result.add(new com.wiki.model.domain.File(ID.nextId(), fileFs.getName(), filesLoc.locationTo(fileFs.getName())));
        }

        return result;
    }

    private Container loadDirectory(File directory, Location location) throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(String.format("File %s doesn't directory", directory.getPath()));
        }

        List<Container> containers = new ArrayList<>();
        List<Document> documents = new ArrayList<>();

        for (File file : prepareFiles(directory)) {
            if (isHidden(file)) {
                // continue
            } else if (file.isDirectory()) {
                containers.add(loadDirectory(file, location.locationTo(file.getName())));
            } else if (isMarkdownFile(file)) {
                documents.add(loadDocument(file, location.locationTo(file.getName())));
            }
        }

        return new Container(ID.nextId(), directory.getName(), containers, documents, location);
    }

    private Document loadDocument(File file, Location location) throws IOException {
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

        return new Document(id, name, content, contents, location);
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
        else if (line.startsWith("###### ")) return true;
        return false;
    }

    private boolean isIndexLine(String line) {
        if (line.startsWith("[//]:")) return true;
        else return false;
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

    private static boolean isHidden(File file) {
        return file.getName().startsWith(".");
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

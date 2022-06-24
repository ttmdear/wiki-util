package com.wiki.app.service.impl;

import com.wiki.app.service.LoadDocumentService;
import com.wiki.app.service.LoadService;
import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.Container;
import com.wiki.model.domain.Document;
import com.wiki.model.domain.ID;
import com.wiki.model.domain.Location;
import com.wiki.model.domain.Wiki;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SingleLoadService implements LoadService {
    private final ResolvePathService resolvePathService;
    private final LoadDocumentService loadDocumentService;

    @Inject
    public SingleLoadService(ResolvePathService resolvePathService, LoadDocumentService loadDocumentService) {
        this.resolvePathService = resolvePathService;
        this.loadDocumentService = loadDocumentService;
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

    @Override
    public Wiki load() throws IOException {
        return load(new File(resolvePathService.resolveWikiPath()));
    }

    public Wiki load(File root) throws IOException {
        Location location = Location.of("");

        return new Wiki(loadDirectory(root, location), loadFiles(root, location), resolvePathService);
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
        Document document = new Document(ID.nextId(), location);
        loadDocumentService.load(document, file);
        return document;
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
}

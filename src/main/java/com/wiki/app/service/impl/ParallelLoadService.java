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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelLoadService implements LoadService {
    private final ResolvePathService resolvePathService;
    private final LoadDocumentService loadDocumentService;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Inject
    public ParallelLoadService(ResolvePathService resolvePathService, LoadDocumentService loadDocumentService) {
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
    public Wiki load() throws IOException, ExecutionException, InterruptedException {
        return load(new File(resolvePathService.resolveWikiPath()));
    }

    public Wiki load(File root) throws IOException, ExecutionException, InterruptedException {
        Location location = Location.of("");

        // The list of task to run parallel.
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        Container container = loadDirectory(root, location, tasks);

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).get();

        executor.shutdown();
        return new Wiki(container, loadFiles(root, location), resolvePathService);
    }

    private List<com.wiki.model.domain.File> loadFiles(File directory, Location location) {
        List<File> files = prepareFiles(directory);

        if (files.isEmpty()) {
            return new ArrayList<>();
        }

        Optional<File> filesOpt = files.stream()
            .filter(file -> file.isDirectory() && file.getName().equals(".files"))
            .findFirst();

        if (filesOpt.isEmpty()) {
            return new ArrayList<>();
        }

        List<com.wiki.model.domain.File> result = new ArrayList<>();
        Location filesLoc = location.locationTo(".files");

        for (File fileFs : prepareFiles(filesOpt.get())) {
            result.add(new com.wiki.model.domain.File(ID.nextId(), fileFs.getName(), filesLoc.locationTo(fileFs.getName())));
        }

        return result;
    }

    private Container loadDirectory(File directory, Location location, List<CompletableFuture<?>> tasks) {
        List<Container> containers = new ArrayList<>();
        List<Document> documents = new ArrayList<>();

        for (File file : prepareFiles(directory)) {
            if (isHidden(file)) continue;

            if (file.isDirectory()) {
                containers.add(loadDirectory(file, location.locationTo(file.getName()), tasks));
            } else if (isMarkdownFile(file)) {
                documents.add(loadDocument(file, location.locationTo(file.getName()), tasks));
            }
        }

        return new Container(ID.nextId(), directory.getName(), containers, documents, location);
    }

    private Document loadDocument(File file, Location location, List<CompletableFuture<?>> tasks) {
        Document document = new Document(ID.nextId(), location);

        tasks.add(CompletableFuture.runAsync(() -> {
            try {
                loadDocumentService.load(document, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor));

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

package com.wiki.model.domain;

import com.wiki.app.service.ResolvePathService;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wiki {
    private final Container container;
    private final List<File> files;
    private final Map<Index, IndexEntry> indexes = new HashMap<>();
    private final ResolvePathService resolvePathService;

    public Wiki(Container container, List<File> files, ResolvePathService resolvePathService) {
        this.container = container;
        this.files = files;
        this.resolvePathService = resolvePathService;

        loadIndexes();
    }

    public List<Index> getIndexes() {
        return new ArrayList<>(indexes.keySet());
    }

    public Container getContainer() {
        return container;
    }

    public List<File> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public void travel(TravelConsumer travelConsumer) {
        travel(container, travelConsumer);
    }

    public void travel(Container container, TravelConsumer travelConsumer) {
        for (Container containerInner : container.getContainers()) {
            travelConsumer.onContainer(containerInner, container);
            travel(containerInner, travelConsumer);
        }

        for (Document document : container.getDocuments()) {
            travelConsumer.onDocument(document, container);
            travelConsumer.onContent(document.getContent(), document);

            for (Content content : document.getContents()) {
                travelConsumer.onContent(content, document);
            }
        }
    }

    public String search(String key) {
        Index index = Index.of(key);
        if (indexes.containsKey(index)) {
            IndexEntry entry = indexes.get(index);
            return entry.document.getContent(entry.content);
        } else {
            return null;
        }
    }

    private void loadIndexes() {
        travel(new TravelConsumer() {
            @Override
            public void onContainer(Container container, Container parent) {

            }

            @Override
            public void onDocument(Document document, Container container) {

            }

            @Override
            public void onContent(Content content, Document document) {
                Index index = document.getContentIndex(content);

                if (index != null) {
                    indexes.put(index, new IndexEntry(document, content));
                }
            }
        });
    }

    public ValidateError validate() {
        List<String> refs = new ArrayList<>();

        travel(new TravelConsumer() {
            @Override
            public void onContainer(Container container, Container parent) {
            }

            @Override
            public void onDocument(Document document, Container container) {
                validateRefs(document, document.getContent(), refs);
            }

            @Override
            public void onContent(Content content, Document document) {
                validateRefs(document, content, refs);
            }
        });

        return new ValidateError(refs);
    }

    private void validateRefs(Document document, Content content, List<String> refs) {
        String parent = document.getLocation().getFileParentPath();
        if (content.hasImages()) {
            for (ImageRef image : content.getImages()) {
                if (image.getLink().isFile()) {
                    if (!Files.exists(Path.of(resolvePathService.resolveWikiPath() + parent + "/" + image.getLink()))) {
                        refs.add(image.getLink().toString());
                    }
                }
            }
        }
    }

    private interface TravelConsumer {
        void onContainer(Container container, Container parent);

        void onDocument(Document document, Container container);

        void onContent(Content content, Document document);
    }

    private static class IndexEntry {
        Document document;
        Content content;

        public IndexEntry(Document document, Content content) {
            this.document = document;
            this.content = content;
        }
    }

    @Getter
    public static class ValidateError {
        private final List<String> refs;

        public ValidateError(List<String> refs) {
            this.refs = refs;
        }

        public boolean hasErrors() {
            return !refs.isEmpty();
        }
    }
}

package com.wiki.model.domain;

import com.wiki.model.exceptions.LoadException;

import java.util.*;

public class Wiki {
    private final Container container;
    private final List<File> files;
    private final Map<Index, IndexEntry> indexes = new HashMap<>();

    public Wiki(Container container, List<File> files) {
        this.container = container;
        this.files = files;

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
}

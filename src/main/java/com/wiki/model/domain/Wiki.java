package com.wiki.model.domain;

import com.wiki.model.exceptions.LoadException;

import java.util.*;

public class Wiki {
    private final Container container;
    private final List<File> files;
    private final Map<Index, Content> indexes = new HashMap<>();

    public Wiki(Container container, List<File> files) {
        this.container = container;
        this.files = files;

        loadIndexes();
    }

    public Map<Index, Content> getIndexes() {
        return indexes;
    }

    public Container getContainer() {
        return container;
    }

    public List<File> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public List<IndexEntry> getIndexEntries() {
        List<IndexEntry> indexEntries = new ArrayList<>();

        travel(new TravelConsumer() {
            @Override
            public void onContainer(Container container, Container parent) {

            }

            @Override
            public void onDocument(Document document, Container container) {

            }

            @Override
            public void onContent(Content content, Document document) {
                if (content.hasIndex()) {
                    indexEntries.add(IndexEntry.of(content.getIndex(), document.getIndex(), content));
                }
            }
        });

        return indexEntries;
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
            return indexes.get(index).getContent();
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
                    indexes.put(index, content);
                }
            }
        });
    }

    private interface TravelConsumer {
        void onContainer(Container container, Container parent);

        void onDocument(Document document, Container container);

        void onContent(Content content, Document document);
    }
}

package com.wiki.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wiki {
    private final Container container;
    private final List<File> files;

    public Wiki(Container container, List<File> files) {
        this.container = container;
        this.files = files;
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

    private interface TravelConsumer {
        void onContainer(Container container, Container parent);

        void onDocument(Document document, Container container);

        void onContent(Content content, Document document);
    }
}

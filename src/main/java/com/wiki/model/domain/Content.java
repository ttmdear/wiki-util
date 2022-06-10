package com.wiki.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Content {
    private ID id;
    private Head head;
    private Index index;
    private String content;
    private List<ImageRef> images;
    private List<LinkRef> links;

    public Content(ID id, Head head, Index index, String content, List<ImageRef> image, List<LinkRef> link) {
        this.id = id;
        this.head = head;
        this.index = index;
        this.content = content;
        this.images = image;
        this.links = link;
    }

    public static Content createEmpty(ID nextId) {
        return new Content(nextId, Head.createEmpty(), null, null, new ArrayList<>(), new ArrayList<>());
    }

    public static Content createEmpty(ID nextId, Index index) {
        return new Content(nextId, Head.createEmpty(), index, null, new ArrayList<>(), new ArrayList<>());
    }

    public ID getId() {
        return id;
    }

    public Head getHead() {
        return head;
    }

    public Index getIndex() {
        return index;
    }

    public String getContent() {
        return content;
    }

    public List<ImageRef> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<LinkRef> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public boolean hasIndex() {
        return index != null;
    }

    public boolean hasLinks() {
        return !links.isEmpty();
    }

    public boolean hasImages() {
        return !images.isEmpty();
    }
}

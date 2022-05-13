package com.wiki.domain;

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
}

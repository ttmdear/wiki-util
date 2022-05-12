package com.wiki.domain;

public class Content {
    private ID id;
    private Head head;
    private Index index;
    private String content;

    public Content(ID id, Head head, Index index, String content) {
        this.id = id;
        this.head = head;
        this.index = index;
        this.content = content;
    }
}

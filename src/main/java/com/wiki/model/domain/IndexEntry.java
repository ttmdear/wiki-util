package com.wiki.model.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class IndexEntry {
    Index index;
    Index parent;
    Content content;

    private IndexEntry(Index index, Index parent, Content content) {
        this.index = index;
        this.parent = parent;
        this.content = content;
    }
}

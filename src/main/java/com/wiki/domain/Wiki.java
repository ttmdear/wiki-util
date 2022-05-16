package com.wiki.domain;

import java.util.List;

public class Wiki {
    private final Container container;
    private final List<File> files;

    public Wiki(Container container, List<File> files) {
        this.container = container;
        this.files = files;
    }
}

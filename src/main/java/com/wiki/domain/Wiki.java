package com.wiki.domain;

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
}

package com.wiki.wiki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileLink {

    @Getter
    private final String title;

    @Getter
    private final String path;

    @Getter
    private final String expresion;

    @Getter
    private final Target target;

    @Getter
    private final Type type;

    public enum Target {
        FILE, IMAGE
    }

    public enum Type {
        HTTP, FILE
    }

}

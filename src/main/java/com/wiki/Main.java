package com.wiki;

import com.google.inject.Guice;
import com.wiki.app.CliLauncher;
import com.wiki.app.Components;

public class Main {
    public static void main(String[] args) {
        Guice.createInjector(new Components()).getInstance(CliLauncher.class).run(args);
    }
}

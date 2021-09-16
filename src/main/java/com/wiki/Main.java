package com.wiki;

import com.wiki.cli.CliApp;

public class Main {
    public static void main(String[] args) {
        new CliApp(new App()).run(args);
    }
}

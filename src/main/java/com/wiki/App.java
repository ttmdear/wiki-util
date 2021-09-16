package com.wiki;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wiki.bootstrap.Components;
import com.wiki.bootstrap.Configuration;
import com.wiki.cli.CommandName;
import com.wiki.reorganize.ChangeFileReader;
import com.wiki.reorganize.ChangeFileWriter;
import com.wiki.reorganize.WikiSubmitter;
import com.wiki.util.IDUtil;
import com.wiki.validation.WikiValidator;
import com.wiki.wiki.Wiki;
import com.wiki.wiki.WikiLoader;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

public class App {
    private static final Injector INJECTOR = Guice.createInjector(new Components());

    private static void printErrors(List<String> errors) {
        if (errors.isEmpty()) {
            System.out.println("No errors");
        } else {
            int i = 0;
            for (String error : errors) {
                i++;
                System.out.println(format("%s %s", i, error));
            }
        }
    }

    public void run(CommandName commandName) throws IOException {
        switch (commandName) {
            case REORGANIZE:
                runReorganize();
                break;
            case REORGANIZE_SUBMIT:
                runReorganizeSubmit();
            case VALIDATE:
                validate();
                break;
        }
    }

    private void runReorganize() throws IOException {
        IDUtil.resetIdSeq();

        WikiLoader wikiLoader = INJECTOR.getInstance(WikiLoader.class);
        Wiki wiki = wikiLoader.load(Configuration.WIKI_PATH);

        WikiValidator wikiValidator = INJECTOR.getInstance(WikiValidator.class);
        List<String> errors = wikiValidator.validate(wiki, Configuration.WIKI_PATH, Configuration.WIKI_FILES_PATH);

        if (!errors.isEmpty()) {
            printErrors(errors);

            return;
        }

        ChangeFileWriter changeFileWriter = INJECTOR.getInstance(ChangeFileWriter.class);
        changeFileWriter.write(wiki, Configuration.WIKI_CHANGE_FILE_PATH);
    }

    private void runReorganizeSubmit() throws IOException {
        WikiLoader wikiLoader = INJECTOR.getInstance(WikiLoader.class);
        Wiki sourceWiki = wikiLoader.load(Configuration.WIKI_PATH);

        ChangeFileReader changeFileReader = INJECTOR.getInstance(ChangeFileReader.class);
        Wiki targetWiki = changeFileReader.read(Configuration.WIKI_CHANGE_FILE_PATH);

        WikiSubmitter submitter = INJECTOR.getInstance(WikiSubmitter.class);
        submitter.submit(Configuration.WIKI_PATH, sourceWiki, targetWiki);

        runReorganize();
    }

    private void validate() throws IOException {
        WikiLoader wikiLoader = INJECTOR.getInstance(WikiLoader.class);
        Wiki wiki = wikiLoader.load(Configuration.WIKI_PATH);

        WikiValidator wikiValidator = INJECTOR.getInstance(WikiValidator.class);

        List<String> errors = wikiValidator.validate(wiki, Configuration.WIKI_PATH, Configuration.WIKI_FILES_PATH);

        printErrors(errors);
    }
}

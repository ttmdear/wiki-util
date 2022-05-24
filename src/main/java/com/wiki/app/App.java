package com.wiki.app;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wiki.app.commands.Command;
import com.wiki.app.commands.SearchCommand;
import com.wiki.app.commands.ValidateCommand;
import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.Wiki;
import com.wiki.model.service.LoadService;

import java.io.File;
import java.io.IOException;

public class App {
    private final ResolvePathService resolvePathService;
    private final LoadService loadService;

    @Inject
    public App(ResolvePathService resolvePathService, LoadService loadService) {
        this.resolvePathService = resolvePathService;
        this.loadService = loadService;
    }

    // private static void printErrors(List<String> errors) {
    //     if (errors.isEmpty()) {
    //         System.out.println("No errors");
    //     } else {
    //         int i = 0;
    //         for (String error : errors) {
    //             i++;
    //             System.out.println(format("%s %s", i, error));
    //         }
    //     }
    // }

    // private void runReorganize() throws IOException {
    //     IDUtil.resetIdSeq();

    //     WikiLoader wikiLoader = INJECTOR.getInstance(WikiLoader.class);
    //     Wiki wiki = wikiLoader.load(Configuration.WIKI_PATH);

    //     WikiValidator wikiValidator = INJECTOR.getInstance(WikiValidator.class);
    //     List<String> errors = wikiValidator.validate(wiki, Configuration.WIKI_PATH, Configuration.WIKI_FILES_PATH);

    //     if (!errors.isEmpty()) {
    //         printErrors(errors);

    //         return;
    //     }

    //     ChangeFileWriter changeFileWriter = INJECTOR.getInstance(ChangeFileWriter.class);
    //     changeFileWriter.write(wiki, Configuration.WIKI_CHANGE_FILE_PATH);
    // }

    // private void runReorganizeSubmit() throws IOException {
    //     WikiLoader wikiLoader = INJECTOR.getInstance(WikiLoader.class);
    //     Wiki sourceWiki = wikiLoader.load(Configuration.WIKI_PATH);

    //     ChangeFileReader changeFileReader = INJECTOR.getInstance(ChangeFileReader.class);
    //     Wiki targetWiki = changeFileReader.read(Configuration.WIKI_CHANGE_FILE_PATH);

    //     WikiSubmitter submitter = INJECTOR.getInstance(WikiSubmitter.class);
    //     submitter.submit(Configuration.WIKI_PATH, sourceWiki, targetWiki);

    //     runReorganize();
    // }

    public void validate(ValidateCommand validateCommand) {
        try {
            String wikiPath = resolvePathService.resolveWikiPath();
            File root = new File(resolvePathService.resolveWikiPath());

            Wiki wiki = loadService.load(root);
        } catch (Exception e) {
            errOutput(e);
        }
    }

    public void dispatch(Command command) {
        if (command instanceof ValidateCommand) {
            validate((ValidateCommand) command);
        } else if (command instanceof SearchCommand) {
            search((SearchCommand) command);
        } else {
            errOutput("Unknown command");
        }
    }

    private void search(SearchCommand command) {
        try {
            loadService.load().search(command.getIndex());
        } catch (IOException e) {
            errOutput(e);
        }
    }

    public void errOutput(String message) {
        System.err.println(message);
    }

    public void errOutput(Exception exception) {
        System.err.println(exception.getMessage());
    }
}

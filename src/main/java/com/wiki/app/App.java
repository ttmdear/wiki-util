package com.wiki.app;

import com.google.inject.Inject;
import com.wiki.app.commands.Command;
import com.wiki.app.commands.ReloadCommand;
import com.wiki.app.commands.SearchCommand;
import com.wiki.app.commands.ValidateCommand;
import com.wiki.app.service.LoadService;
import com.wiki.app.service.ReloadService;
import com.wiki.app.service.ResolvePathService;
import com.wiki.model.domain.Wiki;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class App {
    private final ResolvePathService resolvePathService;
    private final LoadService loadService;
    private final ReloadService reloadService;

    @Inject
    public App(ResolvePathService resolvePathService, LoadService loadService, ReloadService reloadService) {
        this.resolvePathService = resolvePathService;
        this.loadService = loadService;
        this.reloadService = reloadService;
    }

    public void validate(ValidateCommand validateCommand) {
        try {
            Wiki wiki = loadService.load();
            Wiki.ValidateError errors = wiki.validate();

            if (errors.hasErrors()) {
                errOutput(errors);
            }
        } catch (Exception e) {
            errOutput(e);
        }
    }

    private void errOutput(Wiki.ValidateError errors) {
        if (!errors.getRefs().isEmpty()) {
            System.out.print("\nThere are incorrect path:");
            for (String ref : errors.getRefs()) {
                System.out.print("\n" + ref);
            }
        }

        if (!errors.getUnassigned().isEmpty()) {
            System.out.print("\nThere are unused files:");
            for (String unused : errors.getUnassigned()) {
                System.out.print(String.format("\nrm \"%s\";", unused));
            }
        }

        System.out.print("\n");
    }

    public void dispatch(Command command) {
        if (command instanceof ValidateCommand) {
            validate((ValidateCommand) command);
        } else if (command instanceof SearchCommand) {
            search((SearchCommand) command);
        } else if (command instanceof ReloadCommand) {
            reload((ReloadCommand) command);
        } else {
            errOutput("Unknown command");
        }
    }

    private void reload(ReloadCommand command) {
        try {
            reloadService.reload();
        } catch (IOException e) {
            errOutput(e);
        }
    }

    private void search(SearchCommand command) {
        try {
            stdOutput(loadService.load().search(command.getIndex()));
        } catch (Exception e) {
            errOutput(e);
        }
    }

    public void errOutput(String message) {
        System.err.println(message);
    }

    public void stdOutput(String message) {
        System.out.println(message);
    }

    public void errOutput(Exception exception) {
        System.err.println(exception.getMessage());
    }
}

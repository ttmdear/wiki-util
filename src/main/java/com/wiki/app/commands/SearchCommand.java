package com.wiki.app.commands;

import com.wiki.app.exceptions.IncorrectCommandException;
import org.apache.commons.cli.CommandLine;

public class SearchCommand extends BaseCommand {

    private String index;

    public SearchCommand() {
    }

    @Override
    public String getCliName() {
        return "search";
    }

    @Override
    public String getCliDescription() {
        return "Search entry by index";
    }

    @Override
    public void load(CommandLine cmd) {
        if (cmd.hasOption("index")) {
            index = cmd.getOptionValue("index");
        } else {
            throw new IncorrectCommandException("The --index option is required to search");
        }
    }

    public String getIndex() {
        return index;
    }
}

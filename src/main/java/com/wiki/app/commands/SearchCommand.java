package com.wiki.app.commands;

public class SearchCommand extends BaseCommand {

    @Override
    public String getCliName() {
        return "search";
    }

    @Override
    public String getCliDescription() {
        return "Search element in Wiki";
    }
}

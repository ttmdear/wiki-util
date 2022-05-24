package com.wiki.app.commands;

public class ReloadCommand extends BaseCommand {

    @Override
    public String getCliName() {
        return "reload";
    }

    @Override
    public String getCliDescription() {
        return "Reload Wiki files";
    }
}

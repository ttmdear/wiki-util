package com.wiki.app.commands;

public class ValidateCommand extends BaseCommand {

    @Override
    public String getCliName() {
        return "validate";
    }

    @Override
    public String getCliDescription() {
        return "Check if Wiki is corrected.";
    }
}

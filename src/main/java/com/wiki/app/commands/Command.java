package com.wiki.app.commands;

import org.apache.commons.cli.CommandLine;

public interface Command {
    String getCliName();

    String getCliDescription();

    void load(CommandLine cmd);
}

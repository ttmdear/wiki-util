package com.wiki.cli;

import com.wiki.App;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CliApp {
    private final App app;

    public CliApp(App app) {
        this.app = app;
    }

    public void run(String[] args) {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("reorganize", false, "Reorganize wiki content.");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            String[] argsCli = cmd.getArgs();

            if (args.length == 0) {
                return;
            }

            CommandName commandName = argToCommandName(argsCli[0]);

            app.run(commandName);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public CommandName argToCommandName(String arg) {
        if (arg == null || arg.isEmpty()) return null;

        String[] splitted = arg.split("-");

        if (splitted.length == 1) {
            CommandName.valueOf(splitted[0].toUpperCase());
        }

        String upperUnderScore = Stream.of(splitted)
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));

        return CommandName.valueOf(upperUnderScore);
    }
}

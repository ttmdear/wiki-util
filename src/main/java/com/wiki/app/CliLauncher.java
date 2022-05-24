package com.wiki.app;

import com.google.inject.Inject;
import com.wiki.app.commands.Command;
import com.wiki.app.commands.ReloadCommand;
import com.wiki.app.commands.SearchCommand;
import com.wiki.app.commands.ValidateCommand;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class CliLauncher {
    private final App app;
    private final List<Command> commands = Arrays.asList(
        new ValidateCommand(),
        new SearchCommand(),
        new ReloadCommand()
    );

    @Inject
    public CliLauncher(App app) {
        this.app = app;
    }

    public void run(String[] args) {
        // create Options object
        Options options = new Options();

        options.addOption(Option.builder("h")
            .longOpt("help")
            .build());

        options.addOption(Option.builder("c")
            .longOpt("command")
            .hasArg()
            .build());

        options.addOption(Option.builder("i")
            .longOpt("index")
            .hasArg()
            .build());

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                printHelp(options);
            } else if (cmd.hasOption("command")) {
                try {
                    app.dispatch(prepareCommand(cmd));
                } catch (IncorrectCommandException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                printHelp(options);
            }
        } catch (Exception e) {
            app.errOutput(e.getMessage());
        }
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("wiki", options);
    }

    public Command prepareCommand(CommandLine cmd) {
        String command = cmd.getOptionValue("command");

        for (Command commandFor : commands) {
            if (commandFor.getCliName().equals(command)) {
                commandFor.load(cmd);
                return commandFor;
            }
        }

        return null;
    }

    public static class WikiParser implements CommandLineParser {

        @Override
        public CommandLine parse(Options options, String[] strings) throws ParseException {
            return null;
        }

        @Override
        public CommandLine parse(Options options, String[] strings, boolean b) throws ParseException {
            return null;
        }
    }
}

package com.wiki.app;

import com.google.inject.Inject;
import com.wiki.app.commands.Command;
import com.wiki.app.commands.SearchCommand;
import com.wiki.app.commands.ValidateCommand;
import com.wiki.model.domain.Wiki;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class CliLauncher {
    private final App app;
    private final List<Command> commands = Arrays.asList(
        new ValidateCommand(),
        new SearchCommand()
    );

    @Inject
    public CliLauncher(App app) {
        this.app = app;
    }

    public void run(String[] args) {
        // create Options object
        Options options = new Options();

        // options.addOption(new Option("c", "command"))

        // add t option
        for (Command command : commands) {
            Option option = new Option(command.getCliName(), true, command.getCliDescription());

            option.setArgs(2);
            option.setArgName("arg1");
            option.setArgName("arg2");

            options.addOption(option);
            // options.addOption(command.getCliName(), false, command.getCliDescription());
        }

        // search --format-html php-java-index

        CommandLineParser parser = new WikiParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            String[] argsCli = cmd.getArgs();

            if (args.length == 0) {
                return;
            }

            app.dispatch(resolveCommand(argsCli[0]));
        } catch (Exception e) {
            app.errOutput(e.getMessage());
        }
    }

    public Command resolveCommand(String arg) {
        if (arg == null || arg.isEmpty()) return null;

        String[] splitted = arg.split("-");

        for (Command command : commands) {
            if (command.getCliName().equals(splitted[0])) {
                return command;
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

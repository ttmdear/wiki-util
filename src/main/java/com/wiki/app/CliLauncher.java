package com.wiki.app;

import com.google.inject.Inject;
import com.wiki.app.commands.Command;
import com.wiki.app.commands.ValidateCommand;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class CliLauncher {
    private final App app;
    private final List<Command> commands = Arrays.asList(
            new ValidateCommand()
    );

    @Inject
    public CliLauncher(App app) {
        this.app = app;
    }

    public void run(String[] args) {
        // create Options object
        Options options = new Options();

        // add t option
        for (Command command : commands) {
            options.addOption(command.getCliName(), false, command.getCliDescription());
        }

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            String[] argsCli = cmd.getArgs();

            if (args.length == 0) {
                return;
            }

            app.dispatch(resolveCommnad(argsCli[0]));
        } catch (Exception e) {
            app.errOutput(e.getMessage());
        }
    }

    public Command resolveCommnad(String arg) {
        if (arg == null || arg.isEmpty()) return null;

        String[] splitted = arg.split("-");

        for (Command command : commands) {
            if (command.getCliName().equals(splitted[0])) {
                return command;
            }
        }

        return null;
        // if (splitted.length == 1) {
        //     CommandName.valueOf(splitted[0].toUpperCase());
        // }

        // String upperUnderScore = Stream.of(splitted)
        //         .map(String::toUpperCase)
        //         .collect(Collectors.joining("_"));

        // return CommandName.valueOf(upperUnderScore);
    }
}

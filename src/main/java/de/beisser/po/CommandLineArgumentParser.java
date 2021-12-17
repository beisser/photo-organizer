package de.beisser.po;

import de.beisser.po.exceptions.CommandLineOptionsException;
import org.apache.commons.cli.*;

public class CommandLineArgumentParser {


    private static final String SOURCE_OPTION_SHORT = "s";
    private static final String TARGET_OPTION_SHORT = "t";
    private static final String SOURCE_OPTION_LONG = "source";
    private static final String TARGET_OPTION_LONG = "target";

    ExtractedCommandLineOptions extractDirectoryOptions(String[] args) {
        CommandLine cmd = getCommandLine(args);
        validateCommandLineOptions(cmd);

        return new ExtractedCommandLineOptions(cmd.getOptionValue(SOURCE_OPTION_SHORT), cmd.getOptionValue(TARGET_OPTION_SHORT));
    }

    private void validateCommandLineOptions(CommandLine cmd) {
        if (!cmd.hasOption(SOURCE_OPTION_SHORT) || !cmd.hasOption(TARGET_OPTION_SHORT)) {
            throw new CommandLineOptionsException("Please provide a source and a target directory");
        }
    }

    private CommandLine getCommandLine(String[] args) {
        final Options commandLineOptions = configureExpectedCommandLineOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(commandLineOptions, args);
        } catch (ParseException e) {
            throw new CommandLineOptionsException("Unable to parse provided command line options");
        }
        return commandLine;
    }

    private Options configureExpectedCommandLineOptions() {
        final Options commandLineOptions = new Options();
        commandLineOptions.addOption(SOURCE_OPTION_SHORT, SOURCE_OPTION_LONG, true, "Source directory to copy the photos from");
        commandLineOptions.addOption(TARGET_OPTION_SHORT, TARGET_OPTION_LONG, true, "Target directory to copy the photos to");
        return commandLineOptions;
    }

}

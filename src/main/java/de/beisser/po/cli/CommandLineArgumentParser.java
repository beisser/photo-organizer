package de.beisser.po.cli;

import de.beisser.po.exceptions.CommandLineOptionsException;
import org.apache.commons.cli.*;

import static de.beisser.po.PhotoOrganizerConstants.*;

public class CommandLineArgumentParser {

    public ExtractedCommandLineOptions extractDirectoryOptions(String[] args) {
        CommandLine cmd = getCommandLine(args);
        validateCommandLineOptions(cmd);

        return new ExtractedCommandLineOptions(cmd.getOptionValue(SOURCE_OPTION_SHORT),
                cmd.getOptionValue(TARGET_OPTION_SHORT),
                cmd.getOptionValue(TARGET_OPTION_UNKNOWN_DATE_SHORT));
    }

    private void validateCommandLineOptions(CommandLine cmd) {
        if (!cmd.hasOption(SOURCE_OPTION_SHORT) || !cmd.hasOption(TARGET_OPTION_SHORT) || !cmd.hasOption(TARGET_OPTION_UNKNOWN_DATE_SHORT)) {
            throw new CommandLineOptionsException("Please provide a source and a target directory");
        }
    }

    private CommandLine getCommandLine(String[] args) {
        final Options commandLineOptions = configureExpectedCommandLineOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
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
        commandLineOptions.addOption(TARGET_OPTION_UNKNOWN_DATE_SHORT, TARGET_OPTION_UNKNOWN_DATE_LONG, true, "Target directory to copy photos with an unknown date to");
        return commandLineOptions;
    }

}

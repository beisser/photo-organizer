package de.beisser.po.cli;

public class ExtractedCommandLineOptions {
    private String sourceDirectory;
    private String targetDirectory;

    public ExtractedCommandLineOptions(String sourceDirectory, String targetDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }
}

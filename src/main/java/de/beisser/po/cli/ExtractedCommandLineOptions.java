package de.beisser.po.cli;

public class ExtractedCommandLineOptions {
    private String sourceDirectory;
    private String targetDirectory;
    private String unkownDataDirectory;

    public ExtractedCommandLineOptions(String sourceDirectory, String targetDirectory, String unkownDataDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.unkownDataDirectory = unkownDataDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public String getUnkownDataDirectory() {
        return unkownDataDirectory;
    }
}

package de.beisser.po;

public class PhotoOrganizerStatistics {
    private int newFilesCopied  = 0;
    private int skippedFiles = 0;
    private int totalFilesProcessed = 0;

    public void incrementNewFilesCopied() {
        this.newFilesCopied++;
    }

    public void incrementSkippedFiles() {
        this.skippedFiles++;
    }

    public void incrementTotalFilesProcessed() {
        this.totalFilesProcessed++;
    }

    public int getNewFilesCopied() {
        return newFilesCopied;
    }

    public int getSkippedFiles() {
        return skippedFiles;
    }

    public int getTotalFilesProcessed() {
        return totalFilesProcessed;
    }
}

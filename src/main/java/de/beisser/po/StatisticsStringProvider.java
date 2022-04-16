package de.beisser.po;

import static java.lang.System.lineSeparator;

public class StatisticsStringProvider {
     String getPhotoOrganizerStatistics(PhotoOrganizerStatistics photoOrganizerStatistics) {
        StringBuilder logText = new StringBuilder("Finished organizing photos")
                .append(lineSeparator());

        logText.append("Count of new files copied ")
                .append(photoOrganizerStatistics.getNewFilesCopied())
                .append(lineSeparator());

        logText.append("Count of files skipped ")
                .append(photoOrganizerStatistics.getSkippedFiles())
                .append(lineSeparator());

        logText.append("Count of files skipped with different hash ")
                .append(photoOrganizerStatistics.getSkippedFilesWithDifferentHash())
                .append(lineSeparator());

        logText.append("Count of total files processed ")
                .append(photoOrganizerStatistics.getTotalFilesProcessed())
                .append(lineSeparator());

        return logText.toString();
    }
}

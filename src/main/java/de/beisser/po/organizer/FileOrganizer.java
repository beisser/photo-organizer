package de.beisser.po.organizer;

import de.beisser.po.PhotoOrganizerStatistics;
import de.beisser.po.cli.ExtractedCommandLineOptions;
import de.beisser.po.digest.FileHashProvider;
import de.beisser.po.digest.FileHashProviderImpl;
import de.beisser.po.dto.FileDateDto;
import de.beisser.po.exceptions.CommandLineOptionsException;
import de.beisser.po.exceptions.FileNotCopiedException;
import de.beisser.po.exceptions.RootDirNotCreatedException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class FileOrganizer {

    final private String targetDirectory;
    final private String unknownDateTargetDirectory;
    final private PhotoOrganizerStatistics photoOrganizerStatistics;

    final private FileHashProvider fileHashProvider = new FileHashProviderImpl();

    public FileOrganizer(ExtractedCommandLineOptions extractedCommandLineOptions, PhotoOrganizerStatistics photoOrganizerStatistics) {
        this.targetDirectory = extractedCommandLineOptions.getTargetDirectory();
        this.unknownDateTargetDirectory = extractedCommandLineOptions.getUnkownDataDirectory();
        this.photoOrganizerStatistics = photoOrganizerStatistics;
    }

    public void organize(FileDateDto fileDateDto) {
        File targetDirectoryRoot = new File(targetDirectory);

        if(!fileDateDto.isContainsExifData()) {
            targetDirectoryRoot = new File(unknownDateTargetDirectory);
        }

        final int year = getYear(fileDateDto.getDateCreated());
        final String month = getMonth(fileDateDto.getDateCreated());
        final int day = getDay(fileDateDto.getDateCreated());

        createRootDirectoryIfNotExists(targetDirectoryRoot);

        final Path nestedMonthDirectory = createNestedMonthDirectoryIfNotExists(year, month, day, targetDirectoryRoot);

        manageFileMovement(fileDateDto.getFile(), nestedMonthDirectory);
    }

    private void manageFileMovement(File fileToCopy, Path nestedMonthDirectory) {
        final Path copiedFile = Paths.get(nestedMonthDirectory.toString(), fileToCopy.getName());

        if(!copiedFile.toFile().exists()) {
            try {
                Files.copy(fileToCopy.toPath(), copiedFile);
                photoOrganizerStatistics.incrementNewFilesCopied();
                return;
            } catch (IOException e) {
                throw new FileNotCopiedException("Unable to copy file " + fileToCopy.getName());
            }
        }

        try {
            if(!FileUtils.contentEquals(fileToCopy, copiedFile.toFile())) {
                final String fileToCopyHash = fileHashProvider.getHash(copiedFile.toFile());
                final Path fileWithOtherContent = Paths.get(nestedMonthDirectory.toString(),  fileToCopy.getName() + "_" + fileToCopyHash);
                Files.copy(fileToCopy.toPath(), fileWithOtherContent);
                photoOrganizerStatistics.incrementNewFilesCopied();
                throw new RuntimeException("Found files with same name but different content");
            }

            photoOrganizerStatistics.incrementSkippedFiles();
            System.out.println(fileToCopy.getAbsolutePath() + "->" +  copiedFile.toFile().getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path createNestedMonthDirectoryIfNotExists(int year, String monthValue, int day, File targetDirectoryRoot) {
        final Path yearDirectory = Paths.get(targetDirectoryRoot.toPath().toString(), String.valueOf(year));
        final Path monthDirectory = Paths.get(yearDirectory.toString(), monthValue);
        final Path dayDirectory = Paths.get(monthDirectory.toString(), String.valueOf(day));
        if(!Files.isDirectory(dayDirectory)) {
            final boolean isDirectoryCreatedSuccessful = dayDirectory.toFile().mkdirs();
            if(!isDirectoryCreatedSuccessful) {
                throw new RootDirNotCreatedException("unable to create day directory");
            }
        }

        return dayDirectory;
    }

    private void createRootDirectoryIfNotExists(File targetDirectoryRoot) {
        if(Files.isDirectory(targetDirectoryRoot.toPath())) {
            return;
        }

        final boolean isDirectoryCreatedSuccessful = targetDirectoryRoot.mkdir();
        if(!isDirectoryCreatedSuccessful) {
            throw new RootDirNotCreatedException("unable to create root directory");
        }
    }

    private String getMonth(LocalDateTime dateCreated) {
        return String.format("%02d", dateCreated.getMonthValue());
    }

    private int getYear(LocalDateTime dateCreated) {
        return dateCreated.getYear();
    }

    private int getDay(LocalDateTime dateCreated) {
        return dateCreated.getDayOfMonth();
    }
}

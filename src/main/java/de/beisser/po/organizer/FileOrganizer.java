package de.beisser.po.organizer;

import de.beisser.po.PhotoOrganizerStatistics;
import de.beisser.po.digest.FileHashProvider;
import de.beisser.po.digest.FileHashProviderImpl;
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

    private String targetDirectory;
    private PhotoOrganizerStatistics photoOrganizerStatistics;

    private FileHashProvider fileHashProvider = new FileHashProviderImpl();

    public FileOrganizer(String targetDirectory, PhotoOrganizerStatistics photoOrganizerStatistics) {
        this.targetDirectory = targetDirectory;
        this.photoOrganizerStatistics = photoOrganizerStatistics;
    }

    public void organize(File file, LocalDateTime dateCreated) {
        final int year = getYear(dateCreated);
        final String month = getMonth(dateCreated);

        final File targetDirectoryRoot = new File(targetDirectory);
        createRootDirectoryIfNotExists(targetDirectoryRoot);

        final Path nestedMonthDirectory = createNestedMonthDirectoryIfNotExists(year, month, targetDirectoryRoot);

        manageFileMovement(file, nestedMonthDirectory);
    }

    private void manageFileMovement(File fileToCopy, Path nestedMonthDirectory) {
        final Path copiedFile = Paths.get(nestedMonthDirectory.toString(), fileToCopy.getName());

        if(!copiedFile.toFile().exists()) {
            try {
                Files.copy(fileToCopy.toPath(), copiedFile);
                photoOrganizerStatistics.incrementNewFilesCopied();
                return;
            } catch (IOException e) {
                throw new FileNotCopiedException("Unable to copy fileToCopy");
            }
        }

        try {
            if(!FileUtils.contentEquals(fileToCopy, copiedFile.toFile())) {
                final String fileToCopyHash = fileHashProvider.getHash(copiedFile.toFile());
                final Path fileWithOtherContent = Paths.get(nestedMonthDirectory.toString(),  fileToCopy.getName() + "_" + fileToCopyHash);
                Files.copy(fileToCopy.toPath(), fileWithOtherContent);
                photoOrganizerStatistics.incrementNewFilesCopied();
            }

            photoOrganizerStatistics.incrementSkippedFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path createNestedMonthDirectoryIfNotExists(int year, String monthValue, File targetDirectoryRoot) {
        final Path yearDirectory = Paths.get(targetDirectoryRoot.toPath().toString(), String.valueOf(year));
        final Path monthDirectory = Paths.get(yearDirectory.toString(), monthValue);
        if(!Files.isDirectory(monthDirectory)) {
            final boolean isDirectoryCreatedSuccessful = monthDirectory.toFile().mkdirs();
            if(!isDirectoryCreatedSuccessful) {
                throw new RootDirNotCreatedException("unable to create month directory");
            }
        }

        return monthDirectory;
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
}

package de.beisser.po;

import de.beisser.po.dto.FileDateDto;
import de.beisser.po.exceptions.FileCountNotAvailableException;
import de.beisser.po.exceptions.MetadataNotFoundException;
import de.beisser.po.extractor.MetadataExtractor;
import de.beisser.po.organizer.FileOrganizer;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;

@SpringBootApplication
public class SpringBootConsoleApplication
        implements CommandLineRunner {

    public static void main(String[] args) throws IOException, ParseException {
        SpringApplication.run(SpringBootConsoleApplication.class, args);

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser();
        final ExtractedCommandLineOptions extractedCommandLineOptions = commandLineArgumentParser.extractDirectoryOptions(args);

        final long fileCount = getFileCount(extractedCommandLineOptions);

        final PhotoOrganizerStatistics photoOrganizerStatistics = new PhotoOrganizerStatistics();
        try (ProgressBar pb = new ProgressBar("Photo organizer", fileCount)) {
            final MetadataExtractor metadataExtractor = new MetadataExtractor();
            final FileOrganizer fileOrganizer = new FileOrganizer(extractedCommandLineOptions.getTargetDirectory(), photoOrganizerStatistics);

            Files.walk(Paths.get(extractedCommandLineOptions.getSourceDirectory()))
                    .filter(Files::isRegularFile)
                    .map(file -> convertToFileDate(metadataExtractor, file))
                    .peek(fileDateDto ->  {
                        pb.step();
                        photoOrganizerStatistics.incrementTotalFilesProcessed();
                    })
                    .forEach(fileDateDto -> fileOrganizer.organize(fileDateDto.getFile(), fileDateDto.getDateCreated()));


        }

        logPhotoOrganizerStatistics(photoOrganizerStatistics);

    }

    private static void logPhotoOrganizerStatistics(PhotoOrganizerStatistics photoOrganizerStatistics) {
        StringBuilder logText = new StringBuilder("Finished organizing photos")
                .append(lineSeparator());

        logText.append("Count of new files copied ")
                .append(photoOrganizerStatistics.getNewFilesCopied())
                .append(lineSeparator());

        logText.append("Count of files skipped ")
                .append(photoOrganizerStatistics.getSkippedFiles())
                .append(lineSeparator());

        logText.append("Count of total files processed ")
                .append(photoOrganizerStatistics.getTotalFilesProcessed())
                .append(lineSeparator());

        System.out.println(logText);
    }

    private static long getFileCount(ExtractedCommandLineOptions extractedCommandLineOptions) {
        long fileCount = -1L;
        try (Stream<Path> list = Files.list(Paths.get(extractedCommandLineOptions.getSourceDirectory()))) {
            fileCount = list.count();
        } catch (IOException e) {
            throw new FileCountNotAvailableException("unable to get count of all files");
        }
        return fileCount;
    }

    private static FileDateDto convertToFileDate(MetadataExtractor metadataExtractor, Path file) {
        final Optional<LocalDateTime> dateCreated = metadataExtractor.getDateCreated(file.toFile());
        if (dateCreated.isPresent()) {
            return new FileDateDto(file.toFile(), dateCreated.get());
        } else {
            throw new MetadataNotFoundException("Unable to fetch date");
        }
    }

    @Override
    public void run(String... args) {
    }
}

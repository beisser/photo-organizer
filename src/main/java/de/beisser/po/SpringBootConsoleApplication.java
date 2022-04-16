package de.beisser.po;

import de.beisser.po.cli.CommandLineArgumentParser;
import de.beisser.po.cli.ExtractedCommandLineOptions;
import de.beisser.po.dto.FileDateDto;
import de.beisser.po.exceptions.FileCountNotAvailableException;
import de.beisser.po.exceptions.MetadataNotFoundException;
import de.beisser.po.extractor.MetadataExtractor;
import de.beisser.po.organizer.FileOrganizer;
import me.tongfei.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE_BERLIN_STRING;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootConsoleApplication.class);
    private static final StatisticsStringProvider STATISTICS_STRING_PROVIDER = new StatisticsStringProvider();

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        final ExtractedCommandLineOptions extractedCommandLineOptions = getExtractedCommandLineOptions(args);
        final long fileCount = getFileCount(extractedCommandLineOptions);

        final PhotoOrganizerStatistics photoOrganizerStatistics = new PhotoOrganizerStatistics();
        try (ProgressBar pb = new ProgressBar("Photo organizer", fileCount)) {
            final MetadataExtractor metadataExtractor = new MetadataExtractor();
            final FileOrganizer fileOrganizer = new FileOrganizer(extractedCommandLineOptions, photoOrganizerStatistics);

            Files.walk(Paths.get(extractedCommandLineOptions.getSourceDirectory()))
                    .filter(Files::isRegularFile)
                    .map(file -> convertToFileDate(metadataExtractor, file))
                    .peek(fileDateDto -> {
                        pb.step();
                        photoOrganizerStatistics.incrementTotalFilesProcessed();
                    })
                    .forEach(fileOrganizer::organize);
        }

        LOGGER.info(STATISTICS_STRING_PROVIDER.getPhotoOrganizerStatistics(photoOrganizerStatistics));
    }

    private ExtractedCommandLineOptions getExtractedCommandLineOptions(String[] args) {
        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser();
        return commandLineArgumentParser.extractDirectoryOptions(args);
    }

    private static long getFileCount(ExtractedCommandLineOptions extractedCommandLineOptions) {
        long fileCount;
        try {
            fileCount = Files.walk(Paths.get(extractedCommandLineOptions.getSourceDirectory()))
                    .parallel()
                    .filter(p -> !p.toFile().isDirectory())
                    .count();
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
            BasicFileAttributes basicFileAttributes;
            try {
                basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class);
                LocalDateTime fileTime = basicFileAttributes.lastModifiedTime().toInstant().atZone(ZoneId.of(TIME_ZONE_BERLIN_STRING)).toLocalDateTime();
                return new FileDateDto(file.toFile(), fileTime, false);
            } catch (IOException e) {
                e.printStackTrace();
                throw new MetadataNotFoundException("Unable to fetch any date or file type");
            }
        }
    }

}

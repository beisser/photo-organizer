package de.beisser.po.extractor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import de.beisser.po.exceptions.MetadataNotFoundException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class MetadataExtractor {

    private final List<IDateExtractor> dateExtractors;

    public MetadataExtractor() {
        final ExifIFD0DirectoryDateExtractor exifIFD0DirectoryDateExtractor = new ExifIFD0DirectoryDateExtractor();
        final ExifSubIFDDirectoryDateExtractor exifSubIFDDirectoryDateExtractor = new ExifSubIFDDirectoryDateExtractor();
        final QuickTimeMetadataDirectoryDateExtractor quickTimeMetadataDirectoryDateExtractor = new QuickTimeMetadataDirectoryDateExtractor();
        final QuickTimeDirectoryDateExtractor quickTimeDirectoryDateExtractor = new QuickTimeDirectoryDateExtractor();

        this.dateExtractors = Arrays.asList(
                exifIFD0DirectoryDateExtractor,
                exifSubIFDDirectoryDateExtractor,
                quickTimeMetadataDirectoryDateExtractor,
                quickTimeDirectoryDateExtractor
        );
    }

    public Optional<LocalDateTime> getDateCreated(File file) {
        final Metadata metadata = getFileMetadata(file);
        return fetchOldestDate(metadata);
    }

    private Optional<LocalDateTime> fetchOldestDate(Metadata metadata) {
        return dateExtractors.stream()
                .map(dateExtractor -> dateExtractor.fetchDates(metadata))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(date -> date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime())
                .sorted()
                .findFirst();
    }

    private Metadata getFileMetadata(File file) {
        try {
            return ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            throw new MetadataNotFoundException("Unable to get metadata for file with name " + file.getAbsolutePath(), e);
        }
    }

    private void printAllMetadata(Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }


}

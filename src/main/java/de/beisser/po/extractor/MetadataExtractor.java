package de.beisser.po.extractor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
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
        final MP4DirectoryDateExtractor mp4DirectoryDateExtractor = new MP4DirectoryDateExtractor();

        this.dateExtractors = Arrays.asList(
                exifIFD0DirectoryDateExtractor,
                exifSubIFDDirectoryDateExtractor,
                quickTimeMetadataDirectoryDateExtractor,
                quickTimeDirectoryDateExtractor,
                mp4DirectoryDateExtractor
        );
    }

    public Optional<LocalDateTime> getDateCreated(File file) {
        final Optional<Metadata> metadata = getFileMetadata(file);
        if(metadata.isPresent()) {
            return fetchOldestDate(metadata.get());
        }
        return Optional.empty();
    }

    public void printAllMetadataForFile(File file) {
        final Optional<Metadata> metadata = getFileMetadata(file);
        if(metadata.isPresent()) {
            printAllMetadata(metadata.get());
        }
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

    private Optional<Metadata> getFileMetadata(File file) {
        try {
            return Optional.of(ImageMetadataReader.readMetadata(file));
        } catch (Exception e) {
            return Optional.empty();
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

package de.beisser.po.dto;

import java.io.File;
import java.time.LocalDateTime;

public class FileDateDto {
    final private File file;
    final private LocalDateTime dateCreated;
    final private boolean isContainsExifData;

    public FileDateDto(File file, LocalDateTime dateCreated) {
        this(file, dateCreated, true);
    }

    public FileDateDto(File file, LocalDateTime dateCreated, boolean isContainsExifData) {
        this.file = file;
        this.dateCreated = dateCreated;
        this.isContainsExifData = isContainsExifData;
    }

    public File getFile() {
        return file;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public boolean isContainsExifData() {
        return isContainsExifData;
    }
}

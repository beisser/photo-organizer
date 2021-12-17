package de.beisser.po.dto;

import java.io.File;
import java.time.LocalDateTime;

public class FileDateDto {
    private File file;
    private LocalDateTime dateCreated;

    public FileDateDto(File file, LocalDateTime dateCreated) {
        this.file = file;
        this.dateCreated = dateCreated;
    }

    public File getFile() {
        return file;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
}

package de.beisser.po.exceptions;

public class MetadataNotFoundException extends PhotoOrganizerBaseException{
    public MetadataNotFoundException(String message) {
        super(message);
    }
    public MetadataNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

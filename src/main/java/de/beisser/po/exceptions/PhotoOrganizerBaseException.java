package de.beisser.po.exceptions;

public abstract class PhotoOrganizerBaseException extends RuntimeException{
    public PhotoOrganizerBaseException(String message) {
        super(message);
    }
    public PhotoOrganizerBaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

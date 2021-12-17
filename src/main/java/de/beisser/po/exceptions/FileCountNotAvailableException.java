package de.beisser.po.exceptions;

public class FileCountNotAvailableException extends PhotoOrganizerBaseException{
    public FileCountNotAvailableException(String message) {
        super(message);
    }
    public FileCountNotAvailableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

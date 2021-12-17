package de.beisser.po.exceptions;

public class FileHashingException extends PhotoOrganizerBaseException{
    public FileHashingException(String message) {
        super(message);
    }
    public FileHashingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

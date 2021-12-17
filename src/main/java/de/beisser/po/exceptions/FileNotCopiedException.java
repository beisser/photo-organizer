package de.beisser.po.exceptions;

public class FileNotCopiedException extends PhotoOrganizerBaseException{
    public FileNotCopiedException(String message) {
        super(message);
    }
    public FileNotCopiedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

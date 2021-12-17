package de.beisser.po.exceptions;

public class CommandLineOptionsException extends PhotoOrganizerBaseException{
    public CommandLineOptionsException(String message) {
        super(message);
    }
    public CommandLineOptionsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

package de.beisser.po.exceptions;

public class RootDirNotCreatedException extends PhotoOrganizerBaseException{
    public RootDirNotCreatedException(String message) {
        super(message);
    }
    public RootDirNotCreatedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

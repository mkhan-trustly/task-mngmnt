package se.work.task.management.application.exception;

public class UnsupportedJwtTokenException extends RuntimeException {

    public UnsupportedJwtTokenException(String errorMsg) {
        super(errorMsg);
    }
}

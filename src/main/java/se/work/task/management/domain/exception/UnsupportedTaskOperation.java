package se.work.task.management.domain.exception;

public class UnsupportedTaskOperation extends RuntimeException {

    public UnsupportedTaskOperation(String errorMsg) {
        super(errorMsg);
    }
}

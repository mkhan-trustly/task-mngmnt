package se.work.task.management.domain.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}

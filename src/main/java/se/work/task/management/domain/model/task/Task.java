package se.work.task.management.domain.model.task;

import se.work.task.management.domain.model.task.audit.Auditable;

import java.time.Instant;

public interface Task extends Auditable {

    String getId();
    TaskType getTaskType();

    String getTitle();
    String getDescription();

    TaskUser getAssignee();

    TaskStatus getStatus();

    boolean isCompleted();
    void markAsCompleted();
    Instant getCompletedAt();

    void validateTaskDeletion(TaskUser user);
}

package se.work.task.management.domain.model.task;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import se.work.task.management.domain.exception.UnsupportedTaskOperation;
import se.work.task.management.domain.model.task.audit.Auditable;

import java.time.Instant;

@Data
public abstract class AbstractTask implements Task, Auditable {

    @Id
    private String id;
    private TaskType taskType;
    private String title;
    private String description;

    private TaskUser assignee;
    private Instant deadline;

    private TaskStatus status;
    private Instant completedAt;

    private TaskUser createdBy;
    private TaskUser lastUpdatedBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastUpdated;

    public AbstractTask(TaskType taskType,
                        String title, String description, Instant deadline,
                        TaskUser assignee,
                        TaskUser createdBy, TaskUser lastUpdatedBy) {
        this.taskType = taskType;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignee = assignee;
        this.status = TaskStatus.NEW;

        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }


    @Override
    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    @Override
    public void markAsCompleted() {
        this.status = TaskStatus.COMPLETED;
    }

    public void validateTaskDeletion(TaskUser user) {
        if (getStatus() != TaskStatus.NEW) {
            throw new UnsupportedTaskOperation("Task is in %s state and cannot be deleted".formatted(getStatus()));
        }

        if (!getCreatedBy().equals(user)) {
            throw new UnsupportedTaskOperation("The owner of the task can only delete it.");
        }
    }

}

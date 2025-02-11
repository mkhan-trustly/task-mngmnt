package se.work.task.management.domain.persistence;

import org.springframework.data.mongodb.core.mapping.Document;
import se.work.task.management.domain.model.task.AbstractTask;
import se.work.task.management.domain.model.task.TaskType;
import se.work.task.management.domain.model.task.TaskUser;

import java.time.Instant;

@Document(collection = "tasks")
public class BasicTask extends AbstractTask {

    public BasicTask(
            String title, String description, Instant deadline,
            TaskUser assignee,
            TaskUser createdBy, TaskUser lastUpdatedBy) {
        super(TaskType.BASIC,
                title, description, deadline,
                assignee,
                createdBy, lastUpdatedBy);
    }
}

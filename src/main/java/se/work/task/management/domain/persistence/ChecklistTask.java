package se.work.task.management.domain.persistence;

import org.springframework.data.mongodb.core.mapping.Document;
import se.work.task.management.domain.model.task.TaskUser;
import se.work.task.management.domain.model.task.TaskType;
import se.work.task.management.domain.model.task.AbstractTask;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Document(collection = "tasks")
public class ChecklistTask extends AbstractTask {

    private final List<String> checklistItems;

    public ChecklistTask(TaskUser user, String title, String description, Instant deadline, TaskUser assignee, List<String> checklistItems) {
        super(TaskType.CHECKLIST,
                title, description, deadline, assignee,
                user, user);
        this.checklistItems = checklistItems;
    }

    public List<String> getChecklistItems() {
        return Collections.unmodifiableList(checklistItems);
    }

}

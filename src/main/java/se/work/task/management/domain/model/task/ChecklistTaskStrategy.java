package se.work.task.management.domain.model.task;

import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.domain.persistence.ChecklistTask;

public class ChecklistTaskStrategy implements TaskStrategy {

    @Override
    public Task createTask(TaskUser user, CreateTaskRequestDto dto) {
        return new ChecklistTask(user, dto.title(), dto.description(), dto.deadline(), new TaskUser(dto.assigneeId()), dto.options().checklist());
    }
}

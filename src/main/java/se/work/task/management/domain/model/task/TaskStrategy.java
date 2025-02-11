package se.work.task.management.domain.model.task;

import se.work.task.management.api.request.CreateTaskRequestDto;

public interface TaskStrategy {

    Task createTask(TaskUser taskUser, CreateTaskRequestDto dto);
}

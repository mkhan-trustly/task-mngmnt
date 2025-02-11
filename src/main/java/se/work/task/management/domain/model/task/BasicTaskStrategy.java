package se.work.task.management.domain.model.task;

import se.work.task.management.api.request.CreateTaskRequestDto;
import se.work.task.management.domain.persistence.BasicTask;

import static se.work.task.management.Utility.notNullThenGet;

public class BasicTaskStrategy implements TaskStrategy {

    @Override
    public Task createTask(TaskUser user, CreateTaskRequestDto dto) {
        return new BasicTask(
                dto.title(), dto.description(), dto.deadline(),
                notNullThenGet(dto.assigneeId(), TaskUser::new),
                user, user);
    }
}

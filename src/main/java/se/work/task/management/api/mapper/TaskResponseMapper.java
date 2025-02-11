package se.work.task.management.api.mapper;

import se.work.task.management.api.response.TaskResponseDto;
import se.work.task.management.domain.model.task.Task;

import static se.work.task.management.api.mapper.AuditResponseMapper.mapAudit;

public class TaskResponseMapper {

    public static TaskResponseDto mapTask(Task task) {
        if (task == null) {
            return null;
        }

        return TaskResponseDto.builder()
                .taskId(task.getId())
                .taskType(task.getTaskType().name())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .audit(mapAudit(task))
                .build();
    }
}

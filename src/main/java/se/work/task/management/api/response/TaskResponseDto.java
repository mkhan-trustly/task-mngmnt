package se.work.task.management.api.response;

import lombok.Builder;

@Builder
public record TaskResponseDto(
        String taskId,
        String taskType,
        String title,
        String description,
        String status,
        AuditDto audit) {
}

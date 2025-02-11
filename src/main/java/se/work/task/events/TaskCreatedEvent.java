package se.work.task.events;

import lombok.Builder;

@Builder
public record TaskCreatedEvent(String taskId, String assignedTo, String assignedBy) {
}

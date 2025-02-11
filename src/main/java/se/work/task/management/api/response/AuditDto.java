package se.work.task.management.api.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuditDto(String createdBy, String lastUpdatedBy, Instant createdAt, Instant lastUpdated) {
}

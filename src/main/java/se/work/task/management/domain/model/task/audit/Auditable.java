package se.work.task.management.domain.model.task.audit;

import se.work.task.management.domain.model.task.TaskUser;

import java.time.Instant;

public interface Auditable {

    TaskUser getCreatedBy();
    TaskUser getLastUpdatedBy();

    Instant getCreatedAt();
    Instant getLastUpdated();
}
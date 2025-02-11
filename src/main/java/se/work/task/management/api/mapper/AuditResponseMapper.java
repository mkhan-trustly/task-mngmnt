package se.work.task.management.api.mapper;

import se.work.task.management.api.response.AuditDto;
import se.work.task.management.domain.model.task.audit.Auditable;

public class AuditResponseMapper {

    public static AuditDto mapAudit(Auditable audit) {
        return AuditDto.builder()
                .createdBy(audit.getCreatedBy().getUsername())
                .createdAt(audit.getCreatedAt())
                .lastUpdatedBy(audit.getLastUpdatedBy().getUsername())
                .lastUpdated(audit.getLastUpdated())
                .build();
    }
}

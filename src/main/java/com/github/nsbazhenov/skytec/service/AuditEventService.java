package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import com.github.nsbazhenov.skytec.data.repository.AuditEventsRepository;

public class AuditEventService {
    private final AuditEventsRepository repository;

    public AuditEventService(AuditEventsRepository repository) {
        this.repository = repository;
    }

    public AuditEvent save(AuditEvent auditEvent) {
        return repository.save(auditEvent);
    }
}

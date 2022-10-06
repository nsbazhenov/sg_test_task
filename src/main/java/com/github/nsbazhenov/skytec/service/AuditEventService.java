package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import com.github.nsbazhenov.skytec.data.repository.AuditEventsRepository;

import java.sql.Connection;

/**
 * Service for working with the audit.
 *
 * @author Bazhenov Nikita
 *
 */
public class AuditEventService {
    private final AuditEventsRepository repository;

    public AuditEventService(AuditEventsRepository repository) {
        this.repository = repository;
    }

    /**
     * Processing method of saving audit event.
     */
    public AuditEvent save(AuditEvent auditEvent, Connection connection) {
        return repository.save(auditEvent, connection);
    }
}

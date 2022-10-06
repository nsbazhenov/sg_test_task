package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

/**
 * Entity of the audit event.
 *
 * @author Bazhenov Nikita
 */
@Data
public class AuditEvent {
    long id;
    String eventType;
    long clanId;
    long playerId;
    Boolean resultOperation;
    String descriptionOperation;

    public AuditEvent() {
    }

    public AuditEvent(String eventType, long clanId, long playerId) {
        this.eventType = eventType;
        this.clanId = clanId;
        this.playerId = playerId;
    }
}

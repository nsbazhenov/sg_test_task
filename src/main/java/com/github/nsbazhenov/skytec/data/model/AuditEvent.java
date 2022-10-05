package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

@Data
public class AuditEvent {
    long id;
    String eventType;
    long clanId;
    long playerId;
    Boolean resultOperation;
    String descriptionOperation;

    public AuditEvent(String eventType, long clanId, long playerId) {
        this.eventType = eventType;
        this.clanId = clanId;
        this.playerId = playerId;
    }
}

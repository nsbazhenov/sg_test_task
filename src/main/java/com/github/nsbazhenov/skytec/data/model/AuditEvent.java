package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

import java.util.UUID;

@Data
public class AuditEvent {
    UUID id;
    String eventType;
    UUID clanId;
    UUID playerId;
    Boolean resultOperation;
    String descriptionOperation;

    public AuditEvent(String eventType, UUID clanId, UUID playerId, boolean resultOperation) {
        this.eventType = eventType;
        this.clanId = clanId;
        this.playerId = playerId;
        this.resultOperation = resultOperation;
    }
}

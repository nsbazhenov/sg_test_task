package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

import java.util.UUID;

@Data
public class TakeClanGoldRq {
    UUID clanId;
    UUID playerId;
    long value;
}

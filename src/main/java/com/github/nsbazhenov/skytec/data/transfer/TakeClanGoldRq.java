package com.github.nsbazhenov.skytec.data.transfer;

import lombok.Data;

@Data
public class TakeClanGoldRq {
    long clanId;
    long playerId;
    long value;
}

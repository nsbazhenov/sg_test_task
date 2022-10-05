package com.github.nsbazhenov.skytec.data.transfer;

import lombok.Data;

@Data
public class AddClanBalanceRq {
    long clanId;
    long playerId;
    long value;
}

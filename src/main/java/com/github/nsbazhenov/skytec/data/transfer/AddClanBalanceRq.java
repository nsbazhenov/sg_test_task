package com.github.nsbazhenov.skytec.data.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nsbazhenov.skytec.data.exception.ValidationException;
import lombok.Data;

@Data
public class AddClanBalanceRq {
    long clanId;
    long playerId;
    long value;

    public AddClanBalanceRq(@JsonProperty("clanId") long clanId, @JsonProperty("playerId") long playerId,
                            @JsonProperty("value") long value) {
        if (value > 0) {
            this.clanId = clanId;
            this.playerId = playerId;
            this.value = value;
        } else {
            throw new ValidationException("Value < 0");
        }
    }
}

package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Clan {
    private UUID id;
    private String name;
    private long gold;
}

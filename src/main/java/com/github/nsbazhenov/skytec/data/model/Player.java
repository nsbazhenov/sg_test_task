package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    UUID id;
    String name;
    String login;
    String password;
    long gold;
}

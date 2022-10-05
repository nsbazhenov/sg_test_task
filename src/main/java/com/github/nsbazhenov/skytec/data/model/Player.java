package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

@Data
public class Player {
    long id;
    String name;
    String login;
    String password;
    long gold;
}

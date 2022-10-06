package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

/**
 * Entity of the player.
 *
 * @author Bazhenov Nikita
 *
 */
@Data
public class Player {
    long id;
    String name;
    String login;
    String password;
    long gold;
}

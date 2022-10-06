package com.github.nsbazhenov.skytec.data.model;

import lombok.Data;

/**
 * Entity of the clan.
 *
 * @author Bazhenov Nikita
 *
 */
@Data
public class Clan {
    private long id;
    private String name;
    private long gold;
}

package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.repository.PlayerRepository;

/**
 * Service for working with the player.
 *
 * @author Bazhenov Nikita
 *
 */
public class PlayerService {
    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    /**
     * Processing method of getting the player by ID.
     */
    public Player getById(long id) {
        return repository.getById(id);
    }
}

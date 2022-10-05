package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.repository.PlayerRepository;

public class PlayerService {
    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Player getById(long id) {
        return repository.getById(id);
    }
}

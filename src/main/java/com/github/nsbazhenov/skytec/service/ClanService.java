package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.model.AddClanBalanceRq;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.model.TakeClanGoldRq;
import com.github.nsbazhenov.skytec.data.repository.ClanRepository;

import java.util.UUID;

public class ClanService {
    private final ClanRepository repository;

    public ClanService(ClanRepository repository) {
        this.repository = repository;
    }

    public Clan getById(UUID id) {
        return repository.getById(id);
    }

    public Boolean addBalance(AddClanBalanceRq request) {
        return repository.addBalance(request.getClanId(), request.getPlayerId(), request.getValue());
    }

    public Boolean takeAwayBalance(TakeClanGoldRq request) {
        return repository.takeGold(request.getClanId(), request.getPlayerId(), request.getValue());
    }
}

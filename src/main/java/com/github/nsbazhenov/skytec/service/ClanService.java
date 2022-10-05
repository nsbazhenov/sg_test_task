package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.transfer.AddClanBalanceRq;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.transfer.TakeClanGoldRq;
import com.github.nsbazhenov.skytec.data.repository.ClanRepository;

import java.util.List;

public class ClanService {
    private final ClanRepository repository;

    public ClanService(ClanRepository repository) {
        this.repository = repository;
    }

    public Clan getById(long id) {
        return repository.getById(id);
    }

    public List<Clan> getAll() {
        return repository.getAll();
    }

    public boolean addBalance(AddClanBalanceRq request) {
        return repository.addGold(request.getClanId(), request.getPlayerId(), request.getValue());
    }

    public boolean takeAwayBalance(TakeClanGoldRq request) {
        return repository.takeGold(request.getClanId(), request.getPlayerId(), request.getValue());
    }
}

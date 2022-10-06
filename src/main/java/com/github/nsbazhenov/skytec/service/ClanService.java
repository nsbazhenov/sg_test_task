package com.github.nsbazhenov.skytec.service;

import com.github.nsbazhenov.skytec.data.transfer.AddClanBalanceRq;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.transfer.TakeClanGoldRq;
import com.github.nsbazhenov.skytec.data.repository.ClanRepository;

import java.util.List;

/**
 * Service for working with the сlan.
 *
 * @author Bazhenov Nikita
 *
 */
public class ClanService {
    private final ClanRepository repository;

    public ClanService(ClanRepository repository) {
        this.repository = repository;
    }

    /**
     * Processing method of getting the clan by ID.
     */
    public Clan getById(long id) {
        return repository.getById(id);
    }


    /**
     * Processing method of getting clans.
     */
    public List<Clan> getAll() {
        return repository.getAll();
    }

    /**
     * Processing method of adding gold to the clan.
     */
    public boolean addBalance(AddClanBalanceRq request) {
        return repository.addGold(request.getClanId(), request.getPlayerId(), request.getValue());
    }

    /**
     * Method of processing the reduction of gold to the clan.
     */
    public boolean takeAwayBalance(TakeClanGoldRq request) {
        return repository.takeGold(request.getClanId(), request.getPlayerId(), request.getValue());
    }
}

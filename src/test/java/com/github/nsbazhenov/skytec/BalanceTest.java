package com.github.nsbazhenov.skytec;

import com.github.nsbazhenov.skytec.config.ProjectConfiguration;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.repository.AuditEventsRepository;
import com.github.nsbazhenov.skytec.data.repository.ClanRepository;
import com.github.nsbazhenov.skytec.data.repository.PlayerRepository;
import com.github.nsbazhenov.skytec.service.AuditEventService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BalanceTest {
    private ClanRepository clanRepository;
    private PlayerRepository playerRepository;
    private ExecutorService executorService;
    private int nThreads;
    private long clanId;
    private long playerId;
    private long value;

    @Before
    public void setup() {
        ProjectConfiguration.loadConfigurations();

        String DB_URL = ProjectConfiguration.getDbUrl();
        String DB_USER = ProjectConfiguration.getDbUser();
        String DB_PASS = ProjectConfiguration.getDbPass();
        nThreads = 16;

        DataSource dataSource = CreateDataBase.dataSource(DB_URL, DB_USER, DB_PASS);
        clanRepository = new ClanRepository(dataSource, new AuditEventService(new AuditEventsRepository(dataSource)));
        playerRepository = new PlayerRepository(dataSource);

        executorService = Executors.newFixedThreadPool(nThreads);

        clanId = 1L;
        playerId = 1L;
        value = 10L;
    }
   @Test
    public void concurrentAddGoldUpdate() throws Exception {
        for (int i = 0; i < nThreads; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    clanRepository.addGold(clanId, playerId, value);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Clan clanByDb = clanRepository.getById(clanId);
        Player playerByDb = playerRepository.getById(playerId);

        Assert.assertEquals(320000, clanByDb.getGold());
        Assert.assertEquals(0L, playerByDb.getGold());
    }

    @Test
    public void concurrentTakeGoldUpdate() throws Exception {
        for (int i = 0; i < nThreads; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    clanRepository.takeGold(clanId, playerId, value);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Clan clanByDb = clanRepository.getById(clanId);
        Player playerByDb = playerRepository.getById(playerId);

        Assert.assertEquals(0, clanByDb.getGold());
        Assert.assertEquals(320000, playerByDb.getGold());
    }
}
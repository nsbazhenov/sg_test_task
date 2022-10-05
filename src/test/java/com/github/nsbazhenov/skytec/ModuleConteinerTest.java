//package com.github.nsbazhenov.skytec;
//
//
//import com.github.nsbazhenov.skytec.config.PropertiesLoader;
//import com.github.nsbazhenov.skytec.data.model.AddClanBalanceRq;
//import com.github.nsbazhenov.skytec.data.model.Clan;
//import com.github.nsbazhenov.skytec.data.model.Player;
//import com.github.nsbazhenov.skytec.data.repository.ClanRepository;
//import com.github.nsbazhenov.skytec.data.repository.PlayerRepository;
//import org.junit.Assert;
//import org.junit.Test;
//
//import javax.sql.DataSource;
//
//import java.io.IOException;
//import java.util.Properties;
//import java.util.UUID;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//class ModuleContainerTest {
//    Properties conf = PropertiesLoader.loadProperties();
//    private final String DB_URL = conf.getProperty("datasource.url");
//    private final String DB_USER = conf.getProperty("datasource.user");
//    private final String DB_PASS = conf.getProperty("datasource.password");
//
//    ModuleContainerTest() throws IOException {
//        //TODO: нужен ли пустой конструктор
//    }
//
//    @Test
//    void concurrentBalanceUpdate() throws Exception {
//
//        int nThreads = 16;
//        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
//        DataSource dataSource = Server.dataSource(DB_URL, DB_USER, DB_PASS);
//
//        ClanRepository clanRepository = new ClanRepository(dataSource);
//        PlayerRepository playerRepository = new PlayerRepository(dataSource);
//        AddClanBalanceRq request = new AddClanBalanceRq();
//        request.setClanId(1L);
//        request.setPlayerId(1L);
//        request.setValue(10L);
//
//        for (int i = 0; i < nThreads; i++) {
//            executorService.execute(() -> {
//                for (int j = 0; j < 1000; j++) {
//                    clanRepository.addBalance(request);
//                }
//            });
//        }
//
//        executorService.shutdown();
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
//
//        Clan clanByDb = clanRepository.getById(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
//        Player playerByDb = playerRepository.getById(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
//
//        Assert.assertEquals(160000, clanByDb.getGold());
//        Assert.assertEquals(0L, playerByDb.getBalance());
//    }
//}
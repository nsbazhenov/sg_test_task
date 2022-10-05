package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.CreateDataBase;
import com.github.nsbazhenov.skytec.config.ProjectConfiguration;
import com.github.nsbazhenov.skytec.data.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

public class PlayerRepositoryTest {
    private PlayerRepository playerRepository;

    @Before
    public void setup() {
        ProjectConfiguration.loadConfigurations();

        String DB_URL = ProjectConfiguration.getDbUrl();
        String DB_USER = ProjectConfiguration.getDbUser();
        String DB_PASS = ProjectConfiguration.getDbPass();

        DataSource dataSource = CreateDataBase.dataSource(DB_URL, DB_USER, DB_PASS);

        playerRepository = new PlayerRepository(dataSource);
    }

    @Test
    public void getByIdTest() {
        Player player = playerRepository.getById(1);

        Assert.assertEquals(1, player.getId());
        Assert.assertEquals("player", player.getName());
        Assert.assertEquals(160000, player.getGold());
    }

    @Test
    public void getByIdNullTest() {
        Player player = playerRepository.getById(5);

        Assert.assertNull(player);
    }
}

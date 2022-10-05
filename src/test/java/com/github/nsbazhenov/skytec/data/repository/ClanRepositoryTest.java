package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.CreateDataBase;
import com.github.nsbazhenov.skytec.config.ProjectConfiguration;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.service.AuditEventService;
import org.h2.jdbc.JdbcSQLNonTransientException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

public class ClanRepositoryTest {
    private ClanRepository clanRepository;

    @Before
    public void setup() {
        ProjectConfiguration.loadConfigurations();

        String DB_URL = ProjectConfiguration.getDbUrl();
        String DB_USER = ProjectConfiguration.getDbUser();
        String DB_PASS = ProjectConfiguration.getDbPass();

        DataSource dataSource = CreateDataBase.dataSource(DB_URL, DB_USER, DB_PASS);

        clanRepository = new ClanRepository(dataSource, new AuditEventService(new AuditEventsRepository(dataSource)));
    }

    @Test
    public void getByIdTest() {
        Clan clan = clanRepository.getById(1);

        Assert.assertEquals(1, clan.getId());
        Assert.assertEquals("clan", clan.getName());
        Assert.assertEquals(160000, clan.getGold());
    }

    @Test
    public void getAllClansTest() {
        List<Clan> clans = clanRepository.getAll();

        Assert.assertEquals(1, clans.size());
        Assert.assertEquals(1, clans.get(0).getId());
        Assert.assertEquals("clan", clans.get(0).getName());
        Assert.assertEquals(160000, clans.get(0).getGold());
    }

    @Test
    public void getByIdNullTest() {
        Clan clan = clanRepository.getById(5);

        Assert.assertNull(clan);
    }
}

package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.CreateDataBase;
import com.github.nsbazhenov.skytec.config.ProjectConfiguration;
import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class AuditEventsRepositoryTest {
    private AuditEventsRepository auditEventsRepository;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        ProjectConfiguration.loadConfigurations();

        String DB_URL = ProjectConfiguration.getDbUrl();
        String DB_USER = ProjectConfiguration.getDbUser();
        String DB_PASS = ProjectConfiguration.getDbPass();

        DataSource dataSource = CreateDataBase.dataSource(DB_URL, DB_USER, DB_PASS);
        connection = dataSource.getConnection();

        auditEventsRepository = new AuditEventsRepository(dataSource);
    }

    @After
    public void after() throws SQLException {
        connection.close();
    }

    @Test
    public void saveTest() {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setEventType("Test");
        auditEvent.setResultOperation(true);
        auditEvent.setDescriptionOperation("ok");
        auditEvent.setClanId(1L);
        auditEvent.setPlayerId(1L);

        AuditEvent auditEventFromDb = auditEventsRepository.save(auditEvent, connection);

        Assert.assertEquals(auditEvent.getId(), auditEventFromDb.getId());
        Assert.assertEquals(auditEvent.getEventType(), auditEventFromDb.getEventType());
        Assert.assertEquals(auditEvent.getResultOperation(), auditEventFromDb.getResultOperation());
        Assert.assertEquals(auditEvent.getDescriptionOperation(), auditEventFromDb.getDescriptionOperation());
        Assert.assertEquals(auditEvent.getClanId(), auditEventFromDb.getClanId());
        Assert.assertEquals(auditEvent.getPlayerId(), auditEventFromDb.getPlayerId());
    }
}

package com.github.nsbazhenov.skytec;

import com.github.nsbazhenov.skytec.config.ProjectConfiguration;
import com.github.nsbazhenov.skytec.data.repository.AuditEventsRepository;
import com.github.nsbazhenov.skytec.data.repository.ClanRepository;
import com.github.nsbazhenov.skytec.data.repository.PlayerRepository;
import com.github.nsbazhenov.skytec.service.AuditEventService;
import com.github.nsbazhenov.skytec.service.ClanService;
import com.github.nsbazhenov.skytec.service.PlayerService;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * A program which for working with clan and player gold.
 *
 * @author Bazhenov Nikita
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ProjectConfiguration.loadConfigurations();
        String DB_URL = ProjectConfiguration.getDbUrl();
        String DB_USER = ProjectConfiguration.getDbUser();
        String DB_PASS = ProjectConfiguration.getDbPass();
        int serverPort = ProjectConfiguration.getServerPort();

        DataSource dataSource = CreateDataBase.dataSource(DB_URL, DB_USER, DB_PASS);

        AuditEventService auditEventService = new AuditEventService(new AuditEventsRepository(dataSource));
        ClanService clanService = new ClanService(new ClanRepository(dataSource, auditEventService));
        PlayerService playerService = new PlayerService(new PlayerRepository(dataSource));

        Server server = Server.createServer(clanService, playerService, serverPort);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}

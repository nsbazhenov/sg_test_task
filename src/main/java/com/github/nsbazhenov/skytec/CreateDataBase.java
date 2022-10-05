package com.github.nsbazhenov.skytec;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class CreateDataBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDataBase.class);
    public static DataSource dataSource(String dbUrl, String dbUser, String dbPass) {
        try {
            JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(dbUrl, dbUser, dbPass);
            URL resource = Main.class.getResource("/data.sql");
            RunScript.execute(jdbcConnectionPool.getConnection(), new FileReader(new File(resource.toURI())));

            LOGGER.info("Date base was successfully created");

            return jdbcConnectionPool;
        } catch (SQLException | URISyntaxException | FileNotFoundException ex) {
            return null;
        }
    }
}

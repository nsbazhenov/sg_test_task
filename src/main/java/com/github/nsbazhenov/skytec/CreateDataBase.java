package com.github.nsbazhenov.skytec;

import com.github.nsbazhenov.skytec.data.status.Error;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Database operation interface.
 *
 * @author Bazhenov Nikita
 */
public class CreateDataBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDataBase.class);

    /**
     * Creating the database.
     */
    public static DataSource dataSource(String dbUrl, String dbUser, String dbPass) {
        try (InputStream inputStream
                     = Main.class.getClassLoader().getResourceAsStream("data.sql")) {
            JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(dbUrl, dbUser, dbPass);

            Reader reader = new InputStreamReader(inputStream);
            RunScript.execute(jdbcConnectionPool.getConnection(), reader);

            LOGGER.info("Database was successfully created");

            return jdbcConnectionPool;
        } catch (SQLException | IOException exception) {
            LOGGER.error(Error.ERROR_CREATING_DB, exception.getMessage());
            return null;
        }
    }
}

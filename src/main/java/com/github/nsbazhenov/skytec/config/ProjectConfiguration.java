package com.github.nsbazhenov.skytec.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectConfiguration.class);

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASS;
    private static String serverPort;

    public static void loadConfigurations() {
        try (InputStream inputStream = ProjectConfiguration.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties configuration = new Properties();
            configuration.load(inputStream);

            DB_URL = configuration.getProperty("datasource.url");
            DB_USER = configuration.getProperty("datasource.user");
            DB_PASS = configuration.getProperty("datasource.password");
            serverPort = configuration.getProperty("server.port");

            LOGGER.info("Loading properties was successful");
        } catch (IOException exception) {
            LOGGER.error("Error occurred:", exception);
        }
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPass() {
        return DB_PASS;
    }

    public static int getServerPort() {
        return Integer.parseInt(serverPort);
    }
}
package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import com.github.nsbazhenov.skytec.data.status.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

public class AuditEventsRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventsRepository.class);

    private static final String SAVE_AUDIT_EVENTS =
            "INSERT INTO AUDIT_EVENTS (EVENT_TYPE, ID_CLAN, ID_PLAYER, RESULT_OPERATION, DESCRIPTION_OPERATION) VALUES (?, ?, ?, ?, ?);";

    private final DataSource dataSource;

    public AuditEventsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AuditEvent save(AuditEvent auditEvent) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_AUDIT_EVENTS)) {

            statement.setString(1, auditEvent.getEventType());
            statement.setLong(2, auditEvent.getClanId());
            statement.setLong(3, auditEvent.getPlayerId());
            statement.setBoolean(4, auditEvent.getResultOperation());
            statement.setString(5, auditEvent.getDescriptionOperation());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.debug(Error.ERROR_CREATING_EVENT);
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    auditEvent.setId(generatedKeys.getLong(1));
                    return auditEvent;
                } else {
                    LOGGER.debug(Error.ERROR_CREATING_EVENT);
                    return null;
                }
            }
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);
            return null;
        }
    }
}

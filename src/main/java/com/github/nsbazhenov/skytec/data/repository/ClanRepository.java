package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.service.AuditEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class ClanRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClanRepository.class);

    private static final String GET_CLAN_BY_ID = "SELECT * FROM CLAN WHERE ID = ?;";

    private static final String ADD_СLAN_BALANCE = "UPDATE CLAN SET GOLD = GOLD + $1 WHERE ID = $2;";

    private static final String TAKE_PLAYER_GOLD = "UPDATE PLAYER SET GOLD = GOLD - $1 WHERE ID = $2 AND (GOLD - $1) >= 0;";

    private static final String ADD_PLAYER_BALANCE = "UPDATE PLAYER SET GOLD = GOLD + $1 WHERE ID = $2;";

    private static final String TAKE_CLAN_GOLD = "UPDATE CLAN SET GOLD = GOLD - $1 WHERE ID = $2 AND (GOLD - $1) >= 0;";

    private final DataSource dataSource;

    private final AuditEventService auditEventService;

    public ClanRepository(DataSource dataSource, AuditEventService auditEventService) {
        this.dataSource = dataSource;
        this.auditEventService = auditEventService;
    }

    public Clan getById(UUID clanId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CLAN_BY_ID)) {

            statement.setObject(1, clanId);

            try (ResultSet resultSet = statement.executeQuery()) {
                Clan clan = resultSetToClan(resultSet);
                LOGGER.info("Found clan: {}", clan);

                return clan;

            } catch (SQLException exception) {
                LOGGER.error("Error occurred:", exception);
                return null;
            }
        } catch (SQLException exception) {
            LOGGER.error("Error occurred:", exception);
            return null;
        }
    }

    public Boolean addBalance(UUID clanId, UUID playerId, long value) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement playerStatement = connection.prepareStatement(TAKE_PLAYER_GOLD)) {
                playerStatement.setObject(1, playerId);
                playerStatement.setLong(2, value);

                int playerAffectedRows = playerStatement.executeUpdate();

                if (playerAffectedRows == 0) {
                    LOGGER.debug("The player does not have enough money");
                    return false;
                }

                try (PreparedStatement clanStatement = connection.prepareStatement(ADD_СLAN_BALANCE)) {
                    clanStatement.setObject(1, clanId);
                    clanStatement.setLong(2, value);

                    int clanAffectedRows = clanStatement.executeUpdate();

                    if (clanAffectedRows == 0) {
                        LOGGER.debug("Couldn't get the clan to add gold");
                        return false;
                    }
                    connection.commit();
                    return true;
                }
            } catch (SQLException exception) {
                connection.rollback();
                LOGGER.error("Error occurred:", exception);
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("Error occurred:", exception);
            return false;
        }
    }

    public Boolean takeGold(UUID clanId, UUID playerId, long value) {
        AuditEvent auditEvent = new AuditEvent("", clanId, playerId, false);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement clanStatement = connection.prepareStatement(TAKE_CLAN_GOLD)) {
                clanStatement.setObject(1, clanId);
                clanStatement.setLong(2, value);

                int clanAffectedRows = clanStatement.executeUpdate();

                if (clanAffectedRows == 0) {
                    auditEvent.setDescriptionOperation("The clan does not have enough money");
                    LOGGER.debug("The clan does not have enough money");
                    return false;
                }

                try (PreparedStatement playerStatement = connection.prepareStatement(ADD_PLAYER_BALANCE)) {
                    playerStatement.setObject(1, playerId);
                    playerStatement.setLong(2, value);

                    int playerAffectedRows = playerStatement.executeUpdate();

                    if (playerAffectedRows == 0) {
                        LOGGER.debug("Couldn't get the player to add gold");
                        return false;
                    }
                    connection.commit();
                    return true;
                }
            } catch (SQLException exception) {
                connection.rollback();
                auditEvent.setDescriptionOperation("Error occurred:" + exception.getMessage());
                LOGGER.error("Error occurred:", exception);
                return false;
            }
        } catch (SQLException exception) {
            auditEvent.setDescriptionOperation("Error occurred:" + exception.getMessage());
            LOGGER.error("Error occurred:", exception);
            return false;
        }
    }

    private Clan resultSetToClan(ResultSet resultSet) throws SQLException {
        Clan clan = new Clan();
        clan.setId((UUID) resultSet.getObject("id"));
        clan.setName(resultSet.getString("name"));
        clan.setGold(resultSet.getLong("gold"));

        return clan;
    }
}

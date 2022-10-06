package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.data.model.AuditEvent;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.status.Error;
import com.github.nsbazhenov.skytec.data.status.Event;
import com.github.nsbazhenov.skytec.service.AuditEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for working with the сlan.
 *
 * @author Bazhenov Nikita
 *
 */
public class ClanRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClanRepository.class);

    private static final String GET_CLAN_BY_ID = "SELECT * FROM CLAN WHERE ID = $1";

    private static final String GET_ALL_CLANS = "SELECT * FROM CLAN;";
    private static final String ADD_CLAN_GOLD = "UPDATE CLAN SET GOLD = GOLD + $1 WHERE ID = $2;";

    private static final String TAKE_PLAYER_GOLD = "UPDATE PLAYER SET GOLD = GOLD - $1 WHERE ID = $2 AND (GOLD - $1) >= 0;";

    private static final String ADD_PLAYER_GOLD = "UPDATE PLAYER SET GOLD = GOLD + $1 WHERE ID = $2;";

    private static final String TAKE_CLAN_GOLD = "UPDATE CLAN SET GOLD = GOLD - $1 WHERE ID = $2 AND (GOLD - $1) >= 0;";

    private final DataSource dataSource;

    private final AuditEventService auditEventService;

    public ClanRepository(DataSource dataSource, AuditEventService auditEventService) {
        this.dataSource = dataSource;
        this.auditEventService = auditEventService;
    }

    /**
     * Processing method of getting the clan by ID.
     */
    public Clan getById(long clanId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CLAN_BY_ID)) {

            statement.setLong(1, clanId);

            try (ResultSet resultSet = statement.executeQuery()) {
                Clan clan = resultSetToClan(resultSet);
                LOGGER.info("Found clan: {}", clan);

                return clan;
            } catch (SQLException exception) {
                LOGGER.error(Error.ERROR_OCCURRED, exception);

                return null;
            }
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);

            return null;
        }
    }

    /**
     * Processing method of getting clans.
     */
    public List<Clan> getAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_CLANS);
             ResultSet resultSet = statement.executeQuery()) {

            return resultSetToClans(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);

            return null;
        }
    }

    /**
     * Processing method of adding gold to the clan.
     */
    public Boolean addGold(long clanId, long playerId, long value) {
        AuditEvent auditEvent = new AuditEvent(Event.ADD_GOLD_TO_THE_CLAN, clanId, playerId);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement playerStatement = connection.prepareStatement(TAKE_PLAYER_GOLD);
                 PreparedStatement clanStatement = connection.prepareStatement(ADD_CLAN_GOLD)) {

                playerStatement.setLong(1, value);
                playerStatement.setLong(2, playerId);
                if (playerStatement.executeUpdate() == 0) {
                    sendEventToAudit(connection, auditEvent, Error.PLAYER_HAS_NO_MONEY, false);
                    LOGGER.debug(Error.PLAYER_HAS_NO_MONEY);

                    return false;
                }

                clanStatement.setLong(1, value);
                clanStatement.setLong(2, clanId);
                int i = clanStatement.executeUpdate();

                if (i == 0) {
                    sendEventToAudit(connection, auditEvent, Error.ERROR_ADD_GOLD, false);
                    LOGGER.debug(Error.ERROR_ADD_GOLD);

                    return false;
                }
                sendEventToAudit(connection,auditEvent, Error.NO_ERROR, true);
                connection.commit();
                return true;

            } catch (SQLException exception) {
                connection.rollback();
                sendEventToAudit(connection, auditEvent, Error.TRANSACTION_ROLLED_BACK, false);
                LOGGER.error(Error.TRANSACTION_ROLLED_BACK, exception);

                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);

            return false;
        }
    }

    /**
     * Method of processing the reduction of gold to the clan.
     */
    public Boolean takeGold(long clanId, long playerId, long value) {
        AuditEvent auditEvent = new AuditEvent(Event.TAKE_GOLD_TO_THE_CLAN, clanId, playerId);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement clanStatement = connection.prepareStatement(TAKE_CLAN_GOLD);
                 PreparedStatement playerStatement = connection.prepareStatement(ADD_PLAYER_GOLD)) {

                clanStatement.setLong(1, value);
                clanStatement.setLong(2, clanId);
                if (clanStatement.executeUpdate() == 0) {
                    sendEventToAudit(connection, auditEvent, Error.CLAN_HAS_NO_MONEY, false);
                    LOGGER.debug(Error.CLAN_HAS_NO_MONEY);

                    return false;
                }

                playerStatement.setLong(1, value);
                playerStatement.setLong(2, playerId);
                if (playerStatement.executeUpdate() == 0) {
                    sendEventToAudit(connection, auditEvent, Error.ERROR_TAKE_GOLD, false);
                    LOGGER.debug(Error.ERROR_TAKE_GOLD);

                    return false;
                }

                connection.commit();
                sendEventToAudit(connection, auditEvent, Error.NO_ERROR, true);

                return true;
            } catch (SQLException exception) {
                connection.rollback();
                sendEventToAudit(connection, auditEvent, Error.TRANSACTION_ROLLED_BACK, false);
                LOGGER.error(Error.TRANSACTION_ROLLED_BACK, exception);

                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);

            return false;
        }
    }

    private Clan resultSetToClan(ResultSet resultSet) throws SQLException {
        Clan clan = new Clan();
        resultSet.first();
        clan.setId(resultSet.getLong("id"));
        clan.setName(resultSet.getString("name"));
        clan.setGold(resultSet.getLong("gold"));

        return clan;
    }

    private List<Clan> resultSetToClans(ResultSet resultSet) throws SQLException {
        List<Clan> clans = new ArrayList<>();

        while (resultSet.next()) {
            Clan clan = new Clan();
            clan.setId(resultSet.getLong("id"));
            clan.setName(resultSet.getString("name"));
            clan.setGold(resultSet.getLong("gold"));

            clans.add(clan);
        }
        return clans;
    }

    private void sendEventToAudit(Connection connection, AuditEvent auditEvent, String description, Boolean result) {
        auditEvent.setDescriptionOperation(description);
        auditEvent.setResultOperation(result);

        auditEventService.save(auditEvent, connection);
    }
}

package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.status.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerRepository.class);

    private static final String GET_PLAYER_BY_ID = "SELECT * FROM PLAYER WHERE ID = ?;";

    private final DataSource dataSource;

    public PlayerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Player getById(long playerId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PLAYER_BY_ID)) {

            statement.setLong(1, playerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                Player player = resultSetToPlayer(resultSet);

                LOGGER.info("Found player: {}", player);
                return player;
            }
        } catch (SQLException exception) {
            LOGGER.error(Error.ERROR_OCCURRED, exception);
            return null;
        }
    }

    private Player resultSetToPlayer(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        resultSet.first();
        player.setId(resultSet.getLong("id"));
        player.setName(resultSet.getString("name"));
        player.setGold(resultSet.getLong("gold"));

        return player;
    }
}

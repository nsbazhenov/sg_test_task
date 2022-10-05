package com.github.nsbazhenov.skytec.data.repository;

import com.github.nsbazhenov.skytec.data.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerRepository.class);

    private static final String GET_PLAYER_BY_ID = "SELECT * FROM PLAYER WHERE ID = ?;";

    private final DataSource dataSource;

    public PlayerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Player getById(UUID playerId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PLAYER_BY_ID)) {

            statement.setObject(1, playerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                Player player = resultSetToPlayer(resultSet);

                LOGGER.info("Found player: {}", player);
                return player;
            }
        } catch (SQLException exception) {
            LOGGER.error("Error occurred:", exception);
            return null;
        }
    }

    private Player resultSetToPlayer(ResultSet resultSet) throws SQLException {
        Player player = new Player();

        player.setId((UUID) resultSet.getObject("id"));
        player.setName(resultSet.getString("name"));
        player.setGold(resultSet.getLong("gold"));

        return player;
    }
}

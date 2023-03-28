package at.htleonding.muehle.db;

import at.htleonding.muehle.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    public void save(Player player) {
        if (player.getId() == null) {
            this.insert(player);
        } else {
            this.update(player);
        }
    }

    public void insert(Player player) {
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "INSERT INTO M_PLAYER (P_NAME) VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, player.getName());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Update of M_PLAYER failed, no rows affected");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    player.setId(keys.getLong(1));
                } else {
                    throw new SQLException("Insert into M_PLAYER failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Player player) {
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "UPDATE M_PLAYER SET P_NAME=? WHERE P_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, player.getName());
            statement.setLong(2, player.getId());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Update of M_PLAYER failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "DELETE FROM M_PLAYER WHERE P_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Deletion of M_PLAYER failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();

        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "SELECT * FROM M_PLAYER";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                Player player = new Player(result.getString("P_NAME"));

                player.setId(result.getLong("P_ID"));

                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    public Player findById(long id) {
        Player player = null;

        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "SELECT * FROM M_PLAYER WHERE P_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                player = new Player(result.getString("P_NAME"));

                player.setId(result.getLong("P_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return player;
    }
}

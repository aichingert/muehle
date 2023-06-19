package at.htlleonding.mill.repositories;

import at.htlleonding.mill.model.Game;
import at.htlleonding.mill.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private DataSource dataSource = Database.getDataSource();

    public void insert(Game game) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO M_GAME (G_WINNER, G_LOSER, G_IS_WINNER_WHITE) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, game.getWinnerId());
            statement.setLong(2, game.getLoserId());
            statement.setBoolean(3, game.isWinnerWhite());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Insert of M_GAME failed, no rows affected");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    game.setGameId(keys.getLong(1));
                } else {
                    throw new SQLException("Insert into M_GAME failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Game> findAllGamesByUserId(long id) {
        List<Game> games = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM M_GAME WHERE G_WINNER = ? OR G_LOSER = ? ORDER BY G_ID DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, id);

            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                Game game = new Game(
                        result.getLong("G_ID"),
                        result.getLong("G_WINNER"),
                        result.getLong("G_LOSER"),
                        result.getBoolean("G_IS_WINNER_WHITE"));

                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public Game findById(long id) {
        Game game = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM M_GAME WHERE G_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                game = new Game(
                        result.getLong("G_ID"),
                        result.getLong("G_WINNER"),
                        result.getLong("G_LOSER"),
                        result.getBoolean("G_IS_WINNER_WHITE")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }
}

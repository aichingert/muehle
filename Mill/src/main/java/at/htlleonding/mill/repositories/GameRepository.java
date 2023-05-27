package at.htlleonding.mill.repositories;

import at.htlleonding.mill.model.Game;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private DataSource dataSource = Database.getDataSource();

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
                        result.getLong("G_LOSER"));

                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }
}

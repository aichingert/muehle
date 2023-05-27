package at.htlleonding.mill.repositories;

import at.htlleonding.mill.model.Replay;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplayRepository {
    private DataSource dataSource = Database.getDataSource();

    public void insert(Replay replay) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO M_REPLAY (R_NTH_MOVE, R_GAME, R_MOVE) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, replay.getNthMove());
            statement.setLong(2, replay.getGameId());
            statement.setLong(3, replay.getMove().getId());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Update of M_REPLAY failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM M_REPLAY WHERE R_GAME=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Deletion of M_REPLAY failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Replay> getAllMovesFromGame(Long gameId) {
        List<Replay> replays = new ArrayList<>();
        MoveRepository moveRepository = new MoveRepository();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT R_NTH_MOVE, R_GAME, R_MOVE FROM M_REPLAY WHERE R_GAME=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, gameId);

            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                Replay replay = new Replay(
                        result.getLong("R_NTH_MOVE"),
                        result.getLong("R_GAME"),
                        moveRepository.findById(result.getLong("R_MOVE"))
                );
                replays.add(replay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replays;
    }
}

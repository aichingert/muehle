package at.htlleonding.mill.repositories;

import at.htlleonding.mill.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Persistent<User> {
    private DataSource dataSource = Database.getDataSource();

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            this.insert(user);
        } else {
            this.update(user);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE M_USER SET U_USERNAME=?, U_PASSWORD=?, U_ALIAS=? WHERE U_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getAlias());
            statement.setLong(4, user.getId());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Update of M_USER failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(User user) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO M_USER (U_USERNAME, U_PASSWORD,U_ALIAS) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getAlias());

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Update of M_USER failed, no rows affected");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                } else {
                    throw new SQLException("Insert into M_USER failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM M_USER WHERE U_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Deletion of M_USER failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT mu.* FROM M_USER mu LEFT OUTER JOIN M_GAME mg ON mu.U_ID = mg.G_WINNER GROUP BY mu.U_ID, mu.U_USERNAME, mu.U_ALIAS, mu.U_PASSWORD ORDER BY count(mg.G_WINNER) DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                User user = new User(
                        result.getString("U_USERNAME"),
                        result.getString("U_PASSWORD"),
                        result.getString("U_ALIAS"));

                user.setId(result.getLong("U_ID"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User findById(long id) {
        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM M_USER WHERE U_ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new User(
                        result.getString("U_USERNAME"),
                        result.getString("U_PASSWORD"),
                        result.getString("U_ALIAS")
                );
                user.setId(result.getLong("U_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}

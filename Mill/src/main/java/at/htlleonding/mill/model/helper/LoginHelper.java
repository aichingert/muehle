package at.htlleonding.mill.model.helper;

import at.htlleonding.mill.db.Database;
import at.htlleonding.mill.db.UserRepository;
import at.htlleonding.mill.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHelper {
    private DataSource dataSource = Database.getDataSource();
    private Long currentUser;
    private static LoginHelper instance;

    public static LoginHelper getInstance() {
        if (instance == null) {
            instance = new LoginHelper();
        }

        return instance;
    }

    private LoginHelper() {}

    public boolean doesUsernameExist(String username) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT count(*) as 'cnt' FROM M_USER WHERE u_username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                if (result.getInt("cnt") == 0) {
                    return false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean doesAliasExist() {
        return false;
    }

    public void insertUser(String username, String alias, String password) {
        UserRepository userRepository = new UserRepository();
        userRepository.insert(new User(username, password, alias));
    }

    public boolean isPasswordCorrect(String username, String password) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT u_id, u_password FROM M_USER WHERE u_username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                if (!result.getString("u_password").equals(password)) {
                    return false;
                }
                this.currentUser = result.getLong("u_id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Long getCurrentUser() {
        return currentUser;
    }
}

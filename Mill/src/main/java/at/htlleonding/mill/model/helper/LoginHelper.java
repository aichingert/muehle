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
    private final int MIN_USERNAME_LENGTH = 3;
    private final int MIN_PASSWORD_LENGTH = 8;
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

    public boolean isValidUser(String username, String password) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT U_ID FROM M_USER WHERE UPPER(U_USERNAME) = ? AND U_PASSWORD = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username.toUpperCase());
            preparedStatement.setString(2, password.toUpperCase());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                this.currentUser = result.getLong("U_ID");
            } else {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public LoginResult insertUserIfValid(String username, String password, String alias) {
        if (username.length() < MIN_USERNAME_LENGTH  || password.length() < MIN_PASSWORD_LENGTH
                || alias.length() < MIN_USERNAME_LENGTH) {
            return LoginResult.TO_SHORT;
        }

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) as 'cnt' FROM M_USER WHERE UPPER(U_USERNAME) = ? OR UPPER(U_ALIAS) = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username.toUpperCase());
            preparedStatement.setString(2, alias.toUpperCase());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next() && result.getInt("cnt") == 0) {
                UserRepository userRepository = new UserRepository();
                userRepository.insert(new User (username, password, alias));
            } else {
                return LoginResult.TAKEN;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return LoginResult.SUCCESS;
    }

    public Long getCurrentUser() {
        return currentUser;
    }
}

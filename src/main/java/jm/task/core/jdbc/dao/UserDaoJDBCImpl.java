package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() throws SQLException {

    }
    @Override
    public void createUsersTable() {
        String creatTable = "CREATE TABLE IF NOT EXISTS users (" +
                            "id BIGINT NOT NULL AUTO_INCREMENT," +
                            "name VARCHAR(45) NOT NULL," +
                            " lastName VARCHAR(45) NOT NULL," +
                            "age TINYINT NOT NULL, PRIMARY KEY (id))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(creatTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void dropUsersTable() {
        String dropUsersTable = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropUsersTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveUser(String name, String lastName, byte age) {
        String saveUser = "INSERT INTO users (NAME,LASTNAME, AGE) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUserById(long id) {
        String removeUser = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeUser)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String string = "SELECT ID, NAME, LASTNAME, AGE FROM users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(string)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge((byte) resultSet.getInt("AGE"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void cleanUsersTable() {
        String cleanTable = "TRUNCATE TABLE users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(cleanTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

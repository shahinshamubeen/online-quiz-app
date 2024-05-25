package dao;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class AuthDao {
    public List<JSONObject> getUsers() {
        List<JSONObject> users = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM quizapp.users")) {

            while (resultSet.next()) {
                JSONObject user = new JSONObject();
                user.put("id", resultSet.getInt("id"));
                user.put("name", resultSet.getString("name"));
                users.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public JSONObject saveUser(String name) {
        JSONObject user = new JSONObject();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("INSERT INTO users (name) VALUES ('" + name + "')", Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.put("id", generatedKeys.getInt(1));
                user.put("name", name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean validateUserCredentials(String username, String password) {
        boolean userExists = false;
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'")) {

            userExists = resultSet.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userExists;
    }
}

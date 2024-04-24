package dao;

import connectiondb.DBUtil;
import objects.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UsersDAO {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USERS_QUERY = "select * from users";
    private static final String READ_USER_QUERY = "select * from users where id = ?";
    private static final String UPDATE_USER = "update users set username = ?, email = ?, password = ? where id = ?";
    private static final String DELETE_USER = "delete * from users where id = ?";


    public User create(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Get the identifier put into the database, then set user object id.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public User read(int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);

            User user = new User();
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");

                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");

                user.setId(id);
                user.setUserName(username);
                user.setPassword(password);
                user.setEmail(email);
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User[] readAll() {
        try (Connection conn = DBUtil.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(READ_USERS_QUERY);

            User[] userArray = new User[]{};

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String username = rs.getString("username");

                User user = new User();
                user.setEmail(email);
                user.setUserName(username);
                user.setId(id);
                user.setPassword(password);

                userArray = addToArray(user, userArray);
            }

            return userArray;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Creates a table copy increased by 1.
        tmpUsers[users.length] = u; // Adds object in the last position.
        return tmpUsers; // Returns new table.
    }

}

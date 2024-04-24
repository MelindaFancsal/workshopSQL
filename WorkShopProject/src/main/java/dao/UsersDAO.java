package dao;

import connectiondb.DBUtil;
import objects.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

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

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}

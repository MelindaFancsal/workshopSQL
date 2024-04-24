import dao.UsersDAO;
import objects.User;

public class Main {

    public static void main(String[] args) {
        UsersDAO usersDAO = new UsersDAO();

        User user = new User();
        user.setUserName("Andreea");
        user.setEmail("andreea@andreea.ro");
        user.setPassword("ljlkjlkj");

       User updatedUser =  usersDAO.create(user);

        System.out.println(updatedUser);
    }
}

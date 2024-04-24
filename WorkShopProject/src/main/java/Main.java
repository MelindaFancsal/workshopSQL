import dao.UsersDAO;
import objects.User;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        UsersDAO usersDAO = new UsersDAO();

        callCreateUser(usersDAO);
        User user = callReadUser(usersDAO);

        //update user
//        user.setEmail("andreea_new_email@andreea.ro");
//        usersDAO.update(user);

        User[] arrayUsers = usersDAO.readAll();
        System.out.println(Arrays.toString(arrayUsers));
    }

    private static User callReadUser(UsersDAO usersDAO) {
        User result = usersDAO.read(1);
        System.out.println(result);

        return result;
    }

    private static void callCreateUser(UsersDAO usersDAO) {
        User user = new User();
        user.setUserName("Jeni");
        user.setEmail("jeni@andreea.ro");
        user.setPassword("iokhjkh");

        User updatedUser = usersDAO.create(user);

        System.out.println(updatedUser);
    }
}

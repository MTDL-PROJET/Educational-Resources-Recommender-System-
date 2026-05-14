package service;

import dao.UserDAO;
import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO =
            new UserDAO();

    public boolean register(

            String fullName,
            String email,
            String password

    ) {

        String hashedPassword =
                BCrypt.hashpw(
                        password,
                        BCrypt.gensalt()
                );

        User user = new User();

        user.setFullName(fullName);

        user.setEmail(email);

        user.setPasswordHash(hashedPassword);

        user.setRole(Role.STUDENT);

        return userDAO.registerUser(user);
    }

    public User login(

            String email,
            String password

    ) {

        User user =
                userDAO.loginUser(email);

        if(user == null) {

            return null;
        }

        boolean passwordMatches =
                BCrypt.checkpw(
                        password,
                        user.getPasswordHash()
                );

        if(passwordMatches) {

            return user;
        }

        return null;
    }
}
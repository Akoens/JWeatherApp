package app.user;

import org.mindrot.jbcrypt.*;
import static app.Main.userDao;

public class UserController {

    // Authenticate the app.user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.getHashedPassword());
    }

    // This method doesn't do anything, it's just included as an example
    public static void setPassword(String username, String oldPassword, String newPassword) {
        if (authenticate(username, oldPassword)) {
            String newSalt = BCrypt.gensalt();
            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
            // Update the app.user salt and password
        }
    }
}

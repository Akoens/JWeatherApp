package app.controller;

import app.model.User;
import org.mindrot.jbcrypt.*;
import static app.Main.userStore;

public class UserController {

    // Authenticate the app.user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userStore.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.getHashedPassword());
    }

    // This method doesn't do anything, it's just included as an example
    public static void setPassword(String email, String oldPassword, String newPassword) {
        if (authenticate(email, oldPassword)) {
            String newHashedPassword = generateHashedPassword(newPassword);
            userStore.updateUserPassword(email ,newHashedPassword);
        }
    }

    public static String generateHashedPassword(String password){ return  BCrypt.hashpw(password, BCrypt.gensalt());}

}

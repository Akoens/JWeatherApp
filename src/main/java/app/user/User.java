package app.user;

public class User {
    private String username;
    private String salt;
    private String hashedPassword;

    public User(String username, String salt, String hashedPassword) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }

    String getHashedPassword() {
        return hashedPassword;
    }

    String getUsername() {
        return username;
    }

    String getSalt() {
        return salt;
    }
}

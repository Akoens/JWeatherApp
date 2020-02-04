package app.user;

public class User {
    private String email;
    private int authLevel;
    private String hashedPassword;

    public User(){}
    public User(String email, String hashedPassword, int authLevel) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.authLevel = authLevel;
    }

    int getAuthLevel(){return authLevel;}
    String getHashedPassword() {
        return hashedPassword;
    }
    String getEmail() {
        return email;
    }

    void setAuthLevel(int authLevel) {
        this.authLevel = authLevel;
    }

    void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    void setEmail(String email) {
        this.email = email;
    }
}

package app.model;

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

    public  int getAuthLevel(){return authLevel;}
    public String getHashedPassword() {
        return hashedPassword;
    }
    public String getEmail() {
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

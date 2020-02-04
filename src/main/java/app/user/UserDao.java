package app.user;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    private static Connection connection;
    private Statement statement;
    private static final String LINK = "jdbc:sqlite:src/main/resources/sqlitedb/Users.db";

    /*
    private final List<User> users = ImmutableList.of(
            //        Username    Salt for hash                    Hashed password (the password is "password" for all users)
            new User("perwendel@gmail.com", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO", 0), //"$2a$10$h.dl5J86rGH7I8bD9bZeZe",
            new User("davidase@gmail.com", "$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdEMLT83E1KDmUhCy", 1), //"$2a$10$e0MYzXyjpJS7Pd0RVvHwHe",
            new User("federico@gmail.com", "$2a$10$E3DgchtVry3qlYlzJCsyxeSK0fftK4v0ynetVCuDdxGVl1obL.ln2", 2) //"$2a$10$E3DgchtVry3qlYlzJCsyxe",
    );
    */

    public UserDao() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(LINK);
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS \"Users\" (\"id\"INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\"email\" TEXT UNIQUE,\"hashed_password\" TEXT,\"auth_level\" INTEGER)");
    }

    public User getUserByEmail(String email) {
        try{
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE email=?");
            pstmt.setString(1,email);
            ResultSet result = pstmt.executeQuery();
            return new User(result.getString("email"),result.getString("hashed_password"), result.getInt("auth_level"));
        } catch (SQLException e){
            System.err.println("GettingUserByEmail Error" + e.getMessage());
        }
        return null;
    }

    public Iterable<String> getAllEmails() throws SQLException {
        ResultSet result = statement.executeQuery("SELECT email FROM Users");
        ArrayList<String> emails = new ArrayList<String>();

        while(result.next()){
            emails.add(result.getString("email"));
        }
        return emails;
    }

    public void addUser(String email, String hashedPassword, int authLevel){

    }
    public void addUsers(User user){

    }

    public void close(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e){
            System.err.println("Connection closing Error" + e.getMessage());
        }


    }
}

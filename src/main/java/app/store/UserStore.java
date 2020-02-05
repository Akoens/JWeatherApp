package app.store;

import app.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserStore {

    private Connection connection;
    private Statement statement;


    public UserStore(Connection connection) {
        this.connection = connection;
    }

    public void createTables() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS \"Users\" (\"id\"INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\"email\" TEXT UNIQUE,\"hashed_password\" TEXT,\"auth_level\" INTEGER)");
        statement.close();
    }

    /*
        Test users:
        usr: perwendel@gmail.com | pass: password | authlvl: 0
        usr: davidase@gmail.com  | pass: password | authlvl: 1
        usr: federico@gmail.com  | pass: password | authlvl: 2
     */
    public void insertTestUsers() throws SQLException {
        statement = connection.createStatement();
        statement.execute(
                "INSERT OR IGNORE INTO `Users`" +
                    "(`id`, `email`, `hashed_password`, `auth_level`)" +
                "VALUES" +
                    "(1, 'perwendel@gmail.com', '$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO', 0)," +
                    "(2, 'davidase@gmail.com', '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdEMLT83E1KDmUhCy', 1)," +
                    "(3, 'federico@gmail.com', '$2a$10$E3DgchtVry3qlYlzJCsyxeSK0fftK4v0ynetVCuDdxGVl1obL.ln2', 2)"
        );
        statement.close();
    }

    public User getUserByEmail(String email) {
        try{
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE email=?");
            pstmt.setString(1,email);
            ResultSet result = pstmt.executeQuery();
            pstmt.close();
            return new User(result.getString("email"),result.getString("hashed_password"), result.getInt("auth_level"));
        } catch (SQLException e){
            System.err.println("GettingUserByEmail Error" + e.getMessage());
        }
        return null;
    }

    public Iterable<String> getAllEmails() {
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT email FROM Users");
            ArrayList<String> emails = new ArrayList<String>();
            while(result.next()){
                emails.add(result.getString("email"));
            }
            statement.close();
            return emails;
        } catch (SQLException e) {
            System.err.println("Failed to get all emails:" + e.getMessage());
        }
        return null;
    }

    //Add new users to the database
    public void addUser(String email, String hashedPassword, int authLevel){

        try{
            PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO Users(email,hashed_password,auth_level)VALUES(?,?,?) ");
            prepareStatement.setString(1, email);
            prepareStatement.setString(2, hashedPassword);
            prepareStatement.setInt(3, authLevel);
            prepareStatement.execute();
        } catch (SQLException e){
            System.err.println("Failed to insert:" + e.getMessage());
        }
    }
    public void addUser(User user){
        try{
            PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO Users(email,hashed_password,auth_level)VALUES(?,?,?) ");
            prepareStatement.setString(1, user.getEmail());
            prepareStatement.setString(2, user.getHashedPassword());
            prepareStatement.setInt(3, user.getAuthLevel());
            prepareStatement.execute();
        } catch (SQLException e){
            System.err.println("Failed to insert:" + e.getMessage());
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e){
            System.err.println("Connection closing Error" + e.getMessage());
        }


    }
}

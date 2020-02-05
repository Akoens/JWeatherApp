package app.store;

import app.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserStore {

    private Connection connection;
    private Statement statement;

    /*
        Test users:
        usr: perwendel@gmail.com | pass: password | authlvl: 0
        usr: davidase@gmail.com  | pass: password | authlvl: 1
        usr: federico@gmail.com  | pass: password | authlvl: 2

     */

    public UserStore(Connection connection) {
        this.connection = connection;
    }

    public void createTables() throws SQLException {
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

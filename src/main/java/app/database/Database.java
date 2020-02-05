package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:users.db";
    private static Connection connection;

    public static Connection getSQLiteConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(SQLITE_CONNECTION_STRING);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }

}

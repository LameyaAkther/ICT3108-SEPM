package birthdaymanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL =
            "jdbc:mysql://localhost:3306/birthday_manager";
    private static final String USER = "root";   // change if needed
    private static final String PASS = "";       // change if needed

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

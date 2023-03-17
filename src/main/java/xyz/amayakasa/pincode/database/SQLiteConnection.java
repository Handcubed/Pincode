package xyz.amayakasa.pincode.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQLite connection.
 */
public interface SQLiteConnection {

    /**
     * Validate SQLite JDBC driver.
     *
     * @throws ClassNotFoundException The SQLite JDBC driver is not found.
     */
    static void validateDriver() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    /**
     * Establish connection to the database.
     *
     * @return connection to the database.
     * @throws SQLException Unable to establish connection.
     */
    static Connection make(File file) throws ClassNotFoundException, SQLException {
        validateDriver();

        final var resource = "jdbc:sqlite:%s".formatted(file.getPath());

        return DriverManager.getConnection(resource);
    }
}

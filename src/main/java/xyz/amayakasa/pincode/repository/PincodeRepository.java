package xyz.amayakasa.pincode.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Pincode repository.
 * Saves pincodes of players.
 */
public class PincodeRepository {

    private final Connection connection;

    /**
     * Pincode repository.
     *
     * @param connection Connection to the database.
     * @throws SQLException If it is not possible to create a table.
     */
    public PincodeRepository(Connection connection) throws SQLException {
        this.connection = connection;

        createPincodeTableIfAbsent();
    }

    /**
     * Create pincodes table if absent.
     *
     * @throws SQLException If it is not possible to create the table.
     */
    private void createPincodeTableIfAbsent() throws SQLException {
        final var statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS pincodes (uniqueId VARCHAR(36) PRIMARY KEY, pincode VARCHAR(4))"
        );

        statement.execute();
    }

    /**
     * Create pincode of player.
     *
     * @param uniqueId Unique identifier.
     * @param pincode  Pincode.
     * @throws SQLException If it is not possible to create a pincode.
     */
    public void createPincode(UUID uniqueId, String pincode) throws SQLException {
        final var statement = connection.prepareStatement("INSERT INTO pincodes (uniqueId, pincode) VALUES (?, ?)");

        statement.setObject(1, uniqueId);
        statement.setString(2, pincode);

        statement.execute();
    }
    /**
     * Get a pincode of player.
     *
     * @param uniqueId Unique identifier.
     * @throws SQLException If it is not possible to get a pincode.
     * @return Pincode or nothing.
     */
    public Optional<String> getPincode(UUID uniqueId) throws SQLException {
        final var statement = connection.prepareStatement("SELECT pincode FROM pincodes WHERE uniqueId = ? LIMIT 1");

        statement.setObject(1, uniqueId);

        statement.setQueryTimeout(5);

        final var resultSet = statement.executeQuery();

        if (!resultSet.next()) return Optional.empty();

        return Optional.ofNullable(resultSet.getString(1));
    }

}

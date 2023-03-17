package xyz.amayakasa.pincode;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.amayakasa.pincode.database.SQLiteConnection;
import xyz.amayakasa.pincode.listener.PincodeBlockerListener;
import xyz.amayakasa.pincode.listener.PincodeDialogueListener;
import xyz.amayakasa.pincode.message.PincodeMessageRegistry;
import xyz.amayakasa.pincode.repository.PincodeRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Protect your game profile.
 * This plugin is designed to protect game profiles when online mode is disabled.
 *
 * @author Yuly Gorbatkov, Ivan Volokitin, Aram Elbadian
 */
@SuppressWarnings("unused")
public final class Pincode extends JavaPlugin {
    private final Set<UUID> nonLoggedUniqueIds = new HashSet<>();
    private Connection connection;


    @Override
    public void onEnable() {
        saveResource("messages.yml", false);

        final var messages = getDataFolder().toPath().resolve("messages.yml").toFile();

        final var messageRegistry = new PincodeMessageRegistry(YamlConfiguration.loadConfiguration(messages));

        final var database = getDataFolder().toPath().resolve("pincodes.sqlite").toFile();

        PincodeRepository repository;

        try {
            connection = SQLiteConnection.make(database);
            repository = new PincodeRepository(connection);
        } catch (ClassNotFoundException | SQLException exception) {
            throw new RuntimeException(exception);
        }

        getServer().getPluginManager().registerEvents(
                new PincodeBlockerListener(nonLoggedUniqueIds),
                this
        );
        getServer().getPluginManager().registerEvents(
                new PincodeDialogueListener(nonLoggedUniqueIds, repository, messageRegistry),
                this
        );
    }

    @Override
    public void onDisable() {
        try {
            if (!connection.isClosed()) connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

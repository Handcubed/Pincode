package xyz.amayakasa.pincode.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.amayakasa.pincode.Pincode;
import xyz.amayakasa.pincode.message.PincodeMessageRegistry;
import xyz.amayakasa.pincode.repository.PincodeRepository;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

public class PincodeDialogueListener implements Listener {

    private final Set<UUID> nonLoggedUniqueIds;
    private final PincodeRepository repository;
    private final PincodeMessageRegistry messageRegistry;

    public PincodeDialogueListener(
            Set<UUID> nonLoggedUniqueIds,
            PincodeRepository repository,
            PincodeMessageRegistry messageRegistry
    ) {
        this.nonLoggedUniqueIds = nonLoggedUniqueIds;
        this.repository = repository;
        this.messageRegistry = messageRegistry;
    }

    private boolean validatePincode(@NotNull String pincode) {
        final var length = pincode.length();

        if (length != 4) return false;

        return IntStream.range(0, length).map(pincode::charAt).allMatch(Character::isDigit);
    }

    @EventHandler
    public final void onPlayerJoin(@NotNull PlayerJoinEvent event) throws SQLException {
        final var player = event.getPlayer();
        final var uniqueId = player.getUniqueId();

        nonLoggedUniqueIds.add(uniqueId);

        final var pincode = repository.getPincode(uniqueId);

        final var plugin = JavaPlugin.getPlugin(Pincode.class);

        final var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (pincode.isPresent()) {
                    player.sendMessage(messageRegistry.getPincodeInputSuggestionMessage());
                } else {
                    player.sendMessage(messageRegistry.getPincodeCreateSuggestionMessage());
                }
            }
        };

        runnable.runTaskLater(plugin, 2);
    }

    @EventHandler
    public final void onPlayerChat(@NotNull AsyncChatEvent event) throws SQLException {
        final var player = event.getPlayer();
        final var uniqueId = player.getUniqueId();

        if (!nonLoggedUniqueIds.contains(uniqueId)) return;

        event.setCancelled(true);

        final var message = event.message();

        final var pincode = PlainTextComponentSerializer.plainText().serialize(message);

        if (!validatePincode(pincode)) {
            player.sendMessage(messageRegistry.getInvalidPincodeMessage());
            return;
        }

        final var knownPincode = repository.getPincode(uniqueId);

        if (knownPincode.isPresent()) {
            if (!knownPincode.get().equalsIgnoreCase(pincode)) {
                player.sendMessage(messageRegistry.getWrongPincodeMessage());
                return;
            }

            player.sendMessage(messageRegistry.getPincodeInputConfirmationMessage());
        } else {
            repository.createPincode(uniqueId, pincode);
            player.sendMessage(messageRegistry.getPincodeCreateConfirmationMessage(pincode));
        }

        nonLoggedUniqueIds.remove(uniqueId);
    }

    @EventHandler
    public final void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        final var uniqueId = event.getPlayer().getUniqueId();

        nonLoggedUniqueIds.remove(uniqueId);
    }
}

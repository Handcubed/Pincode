package xyz.amayakasa.pincode.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class PincodeBlockerListener implements Listener {

    private final Set<UUID> nonLoggedUniqueIds;

    public PincodeBlockerListener(Set<UUID> nonLoggedUniqueIds) {
        this.nonLoggedUniqueIds = nonLoggedUniqueIds;
    }

    @EventHandler
    public final void onPlayerMove(@NotNull PlayerMoveEvent event) {
        final var uniqueId = event.getPlayer().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
    }

    @EventHandler
    public final void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        final var uniqueId = event.getPlayer().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
    }

    @EventHandler
    public final void onPlayerPickupItem(@NotNull EntityPickupItemEvent event) {
        final var uniqueId = event.getEntity().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
    }

    @EventHandler
    public final void onPlayerDropItem(@NotNull EntityDropItemEvent event) {
        final var uniqueId = event.getEntity().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
    }

    @EventHandler
    public final void onPlayerDamageByBlock(@NotNull EntityDamageByBlockEvent event) {
        final var uniqueId = event.getEntity().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
    }

    @EventHandler
    public final void onPlayerDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        final var uniqueId = event.getEntity().getUniqueId();
        final var damagerUniqueId = event.getDamager().getUniqueId();

        if (nonLoggedUniqueIds.contains(uniqueId)) event.setCancelled(true);
        if (nonLoggedUniqueIds.contains(damagerUniqueId)) event.setCancelled(true);
    }
}

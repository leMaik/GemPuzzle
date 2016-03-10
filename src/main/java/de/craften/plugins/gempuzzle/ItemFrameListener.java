package de.craften.plugins.gempuzzle;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Listener for interactions with game item frames.
 */
public class ItemFrameListener implements Listener {
    private final Plugin plugin;

    public ItemFrameListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ITEM_FRAME) {
            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }
}

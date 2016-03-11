package de.craften.plugins.gempuzzle;

import de.craften.plugins.gempuzzle.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
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
        if (event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
            tryMove((ItemFrame) event.getRightClicked());
        }
    }

    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
                event.setDamage(0);
                tryMove((ItemFrame) event.getEntity());
            }
        }
    }

    private boolean tryMove(ItemFrame itemFrame) {
        return tryMove(itemFrame, 0, -1) || tryMove(itemFrame, 0, 1) || tryMove(itemFrame, -1, 0) || tryMove(itemFrame, 1, 0);
    }

    private boolean tryMove(ItemFrame itemFrame, int upDown, int leftRight) {
        Block block = itemFrame.getLocation().getBlock();
        Location neighborLocation = Util.getRelative(
                block,
                itemFrame.getFacing(), upDown, leftRight, 0).getLocation();
        ItemFrame neighbor = Util.getItemFrame(neighborLocation);
        if (neighbor == null) {
            itemFrame.teleport(itemFrame.getLocation().add(neighborLocation).subtract(block.getLocation()));
            return true;
        }
        return false;
    }
}

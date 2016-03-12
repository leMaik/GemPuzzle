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
            if (event.getPlayer().hasMetadata("gempuzzle.create")) {
                event.getPlayer().removeMetadata("gempuzzle.create", plugin);
                if (plugin.getPuzzleAt(event.getRightClicked().getLocation()) == null) {
                    plugin.createPuzzle((ItemFrame) event.getRightClicked());
                    event.getPlayer().sendMessage("Puzzle created.");
                } else {
                    event.getPlayer().sendMessage("This is already a gem puzzle.");
                }
                event.setCancelled(true);
            } else if (event.getPlayer().hasMetadata("gempuzzle.remove")) {
                event.getPlayer().removeMetadata("gempuzzle.remove", plugin);
                Puzzle puzzle = plugin.getPuzzleAt(event.getRightClicked().getLocation());
                if (puzzle != null) {
                    plugin.removePuzzle(puzzle);
                    event.getPlayer().sendMessage("Puzzle removed.");
                } else {
                    event.getPlayer().sendMessage("This isn't a gem puzzle.");
                }
                event.setCancelled(true);
            } else {
                Puzzle puzzle = plugin.getPuzzleAt(event.getRightClicked().getLocation());
                if (puzzle != null) {
                    event.setCancelled(true);
                    tryMove((ItemFrame) event.getRightClicked(), puzzle);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame && event.getDamager() instanceof Player) {
            Puzzle puzzle =
                    plugin.getPuzzleAt(event.getEntity().getLocation());
            if (puzzle != null) {
                event.setCancelled(true);
                event.setDamage(0);
                tryMove((ItemFrame) event.getEntity(), puzzle);
            }
        }
    }

    private boolean tryMove(ItemFrame itemFrame, Puzzle puzzle) {
        return tryMove(puzzle, itemFrame, 0, -1) ||
                tryMove(puzzle, itemFrame, 0, 1) ||
                tryMove(puzzle, itemFrame, -1, 0) ||
                tryMove(puzzle, itemFrame, 1, 0);
    }

    private boolean tryMove(Puzzle puzzle, ItemFrame itemFrame, int upDown, int leftRight) {
        Block block = itemFrame.getLocation().getBlock();
        Location neighborLocation = Util.getRelative(block, itemFrame.getFacing(), upDown, leftRight, 0).getLocation();
        if (puzzle.equals(plugin.getPuzzleAt(neighborLocation))) {
            ItemFrame neighbor = Util.getItemFrame(neighborLocation);
            if (neighbor == null) {
                itemFrame.teleport(itemFrame.getLocation().add(neighborLocation).subtract(block.getLocation()));
                return true;
            }
        }
        return false;
    }
}

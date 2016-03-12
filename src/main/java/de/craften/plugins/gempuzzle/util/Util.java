package de.craften.plugins.gempuzzle.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

/**
 * Utility functions required for the plugin.
 */
public class Util {
    public static Block getRelative(Block block, BlockFace face, int upDown, int leftRight, int frontBack) {
        switch (face) {
            case NORTH:
                return block.getRelative(leftRight, upDown, -frontBack);
            case EAST:
                return block.getRelative(frontBack, upDown, leftRight);
            case SOUTH:
                return block.getRelative(-leftRight, upDown, frontBack);
            case WEST:
                return block.getRelative(-frontBack, upDown, -leftRight);
        }
        throw new IllegalArgumentException("Unsupported BlockFace: " + face);
    }

    public static Location getRelative(Location location, BlockFace face, int upDown, int leftRight, int frontBack) {
        switch (face) {
            case NORTH:
                return location.clone().add(leftRight, upDown, -frontBack);
            case EAST:
                return location.clone().add(frontBack, upDown, leftRight);
            case SOUTH:
                return location.clone().add(-leftRight, upDown, frontBack);
            case WEST:
                return location.clone().add(-frontBack, upDown, -leftRight);
        }
        throw new IllegalArgumentException("Unsupported BlockFace: " + face);
    }

    public static ItemFrame getItemFrame(Location location) {
        for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
            if (nearbyEntity instanceof ItemFrame && nearbyEntity.getLocation().getBlock().getLocation().equals(location)) {
                return (ItemFrame) nearbyEntity;
            }
        }
        return null;
    }
}

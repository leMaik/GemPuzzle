package de.craften.plugins.gempuzzle.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
}

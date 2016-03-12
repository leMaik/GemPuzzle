package de.craften.plugins.gempuzzle;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/**
 * A puzzle.
 */
public class Puzzle {
    private final Location location;
    private final int width;
    private final int height;
    private final BlockFace blockFace;

    public Puzzle(Location location, int width, int height, BlockFace blockFace) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.blockFace = blockFace;
    }

    public Location getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public boolean contains(Location location) {
        if (location.getBlockY() > this.location.getBlockY() || location.getBlockY() <= this.location.getBlockY() - height) {
            return false;
        }

        switch (blockFace) {
            case NORTH:
                return location.getBlockZ() == this.location.getBlockZ() &&
                        location.getBlockX() <= this.location.getBlockX() &&
                        location.getBlockX() > this.location.getBlockX() - width;
            case EAST:
                return location.getBlockX() == this.location.getBlockX() &&
                        location.getBlockZ() <= this.location.getBlockZ() &&
                        location.getBlockZ() > this.location.getBlockZ() - width;
            case SOUTH:
                return location.getBlockZ() == this.location.getBlockZ() &&
                        location.getBlockX() >= this.location.getBlockX() &&
                        location.getBlockX() < this.location.getBlockX() + width;
            case WEST:
                return location.getBlockX() == this.location.getBlockX() &&
                        location.getBlockZ() >= this.location.getBlockZ() &&
                        location.getBlockZ() < this.location.getBlockZ() + width;
            default:
                return false;
        }
    }
}

package de.craften.plugins.gempuzzle;

import de.craften.plugins.gempuzzle.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A puzzle.
 */
public class Puzzle {
    private final Location location;
    private final int width;
    private final int height;
    private final BlockFace blockFace;
    private final Short[] mapIds;
    private final int gemReward;

    public Puzzle(Location location, int width, int height, BlockFace blockFace, Short[] mapIds, int gemReward) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.blockFace = blockFace;
        this.mapIds = mapIds;
        this.gemReward = gemReward;
    }

    public Puzzle(Location location, int width, int height, BlockFace blockFace) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.blockFace = blockFace;
        this.mapIds = getMapIds();
        this.gemReward = 0;
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

    /**
     * Checks if this puzzle is solved.
     *
     * @return true if this puzzle is solved, false if not
     */
    public boolean isSolved() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ItemFrame frame = Util.getItemFrame(Util.getRelative(location, blockFace.getOppositeFace(), -y, x, 0));

                if (frame != null) {
                    if (this.mapIds[y * width + x] == null) {
                        return false;
                    }
                    if (!(frame.getItem() != null &&
                            frame.getItem().getType() == Material.MAP &&
                            frame.getItem().getDurability() == this.mapIds[y * width + x])) {
                        return false;
                    }
                } else if (this.mapIds[y * width + x] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shuffle() {
        //Get all picture frames that make up this puzzle
        ItemFrame[][] frames = new ItemFrame[height][width];
        int emptyX = 0;
        int emptyY = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ItemFrame frame = Util.getItemFrame(Util.getRelative(location, blockFace.getOppositeFace(), -y, x, 0));
                frames[y][x] = frame;
                if (frame == null) {
                    emptyX = x;
                    emptyY = y;
                }
            }
        }

        //Shuffle
        int movesLeft = width * height * 21; //works pretty well
        int prev = -1;
        while (movesLeft > 0) {
            switch (ThreadLocalRandom.current().nextInt(4)) {
                case 0: //down
                    if (emptyY > 0 && prev != 1) {
                        frames[emptyY][emptyX] = frames[emptyY - 1][emptyX];
                        frames[emptyY - 1][emptyX] = null;
                        emptyY--;
                        movesLeft--;
                        prev = 0;
                    }
                    break;
                case 1: //up
                    if (emptyY < height - 1 && prev != 0) {
                        frames[emptyY][emptyX] = frames[emptyY + 1][emptyX];
                        frames[emptyY + 1][emptyX] = null;
                        emptyY++;
                        movesLeft--;
                        prev = 1;
                    }
                    break;
                case 2: //right
                    if (emptyX > 0 && prev != 3) {
                        frames[emptyY][emptyX] = frames[emptyY][emptyX - 1];
                        frames[emptyY][emptyX - 1] = null;
                        emptyX--;
                        movesLeft--;
                        prev = 2;
                    }
                    break;
                case 3: //left
                    if (emptyX < width - 1 && prev != 2) {
                        frames[emptyY][emptyX] = frames[emptyY][emptyX + 1];
                        frames[emptyY][emptyX + 1] = null;
                        emptyX++;
                        movesLeft--;
                        prev = 3;
                    }
                    break;
            }
        }

        //Teleport the item frames
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (frames[y][x] != null) {
                    frames[y][x].teleport(Util.getRelative(location, blockFace.getOppositeFace(), -y, x, 0)
                            .setDirection(new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ())));
                }
            }
        }
    }

    public static Short[] stringToMapIds(String serializedMapIds) {
        String[] idStrings = serializedMapIds.split(",");
        Short[] mapIds = new Short[idStrings.length];
        for (int i = 0; i < idStrings.length; i++) {
            if (idStrings[i].equals("null")) {
                mapIds[i] = null;
            } else {
                mapIds[i] = Short.valueOf(idStrings[i]);
            }
        }
        return mapIds;
    }

    private Short[] getMapIds() {
        Short[] mapIds = new Short[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ItemFrame frame = Util.getItemFrame(Util.getRelative(location, blockFace.getOppositeFace(), -y, x, 0));
                if (frame != null && frame.getItem() != null && frame.getItem().getType() == Material.MAP) {
                    mapIds[y * width + x] = frame.getItem().getDurability();
                } else {
                    mapIds[y * width + x] = null;
                }
            }
        }
        return mapIds;
    }

    public String getMapIdsAsString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mapIds.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            if (mapIds[i] == null) {
                builder.append("null");
            } else {
                builder.append(mapIds[i].toString());
            }
        }
        return builder.toString();
    }

    public int getGemReward() {
        return gemReward;
    }
}

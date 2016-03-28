package de.craften.plugins.gempuzzle;

import de.craften.plugins.gempuzzle.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * A plugin that adds gem puzzles (also known as 15 puzzles), using item frames.
 */
public class Plugin extends JavaPlugin {
    private Collection<Puzzle> puzzles;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        puzzles = new ArrayList<>();
        for (Map puzzleMap : getConfig().getMapList("puzzles")) {
            puzzles.add(new Puzzle(
                    new Location(
                            getServer().getWorld(puzzleMap.get("world").toString()),
                            Double.parseDouble(puzzleMap.get("x").toString()),
                            Double.parseDouble(puzzleMap.get("y").toString()),
                            Double.parseDouble(puzzleMap.get("z").toString())),
                    Integer.parseInt(puzzleMap.get("width").toString()),
                    Integer.parseInt(puzzleMap.get("height").toString()),
                    BlockFace.valueOf(puzzleMap.get("blockFace").toString())
            ));
        }

        getServer().getPluginManager().registerEvents(new ItemFrameListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            if (sender.hasPermission("gempuzzle.manage")) {
                if (args[0].equalsIgnoreCase("create")) {
                    ((Player) sender).setMetadata("gempuzzle.create", new FixedMetadataValue(this, true));
                    ((Player) sender).removeMetadata("gempuzzle.remove", this);
                    ((Player) sender).removeMetadata("gempuzzle.shuffle", this);
                    sender.sendMessage("Now right-click on the top-left item frame to create a gem puzzle.");
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    ((Player) sender).setMetadata("gempuzzle.remove", new FixedMetadataValue(this, true));
                    ((Player) sender).removeMetadata("gempuzzle.create", this);
                    ((Player) sender).removeMetadata("gempuzzle.shuffle", this);
                    sender.sendMessage("Now right-click on a puzzle to remove it.");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("shuffle")) {
                ((Player) sender).setMetadata("gempuzzle.shuffle", new FixedMetadataValue(this, true));
                ((Player) sender).removeMetadata("gempuzzle.create", this);
                ((Player) sender).removeMetadata("gempuzzle.remove", this);
                sender.sendMessage("Now right-click on a puzzle to shuffle it.");
                return true;
            }
        }
        return false;
    }

    public Puzzle getPuzzleAt(Location location) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.contains(location)) {
                return puzzle;
            }
        }
        return null;
    }

    public void createPuzzle(ItemFrame topLeftItemFrame) {
        //auto-detect width
        int width = 1;
        Block block = Util.getRelative(topLeftItemFrame.getLocation().getBlock(), topLeftItemFrame.getAttachedFace(), 0, width, 0);
        while (Util.getItemFrame(block.getLocation()) != null) {
            width++;
            block = Util.getRelative(topLeftItemFrame.getLocation().getBlock(), topLeftItemFrame.getAttachedFace(), 0, width, 0);
        }

        //auto-detect height
        int height = 1;
        block = Util.getRelative(topLeftItemFrame.getLocation().getBlock(), topLeftItemFrame.getAttachedFace(), -height, 0, 0);
        while (Util.getItemFrame(block.getLocation()) != null) {
            height++;
            block = Util.getRelative(topLeftItemFrame.getLocation().getBlock(), topLeftItemFrame.getAttachedFace(), -height, 0, 0);
        }

        puzzles.add(new Puzzle(topLeftItemFrame.getLocation(), width, height, topLeftItemFrame.getFacing()));
        saveConfig();
    }

    public void removePuzzle(Puzzle puzzle) {
        puzzles.remove(puzzle);
        saveConfig();
    }

    @Override
    public void saveConfig() {
        List<Map<String, Object>> puzzleMaps = new ArrayList<>(puzzles.size());

        for (Puzzle puzzle : puzzles) {
            Map<String, Object> puzzleMap = new HashMap<>();
            puzzleMaps.add(puzzleMap);

            puzzleMap.put("world", puzzle.getLocation().getWorld().getName());
            puzzleMap.put("x", puzzle.getLocation().getBlockX());
            puzzleMap.put("y", puzzle.getLocation().getBlockY());
            puzzleMap.put("z", puzzle.getLocation().getBlockZ());
            puzzleMap.put("width", puzzle.getWidth());
            puzzleMap.put("height", puzzle.getHeight());
            puzzleMap.put("blockFace", puzzle.getBlockFace().name());
        }

        getConfig().set("puzzles", puzzleMaps);
        super.saveConfig();
    }
}

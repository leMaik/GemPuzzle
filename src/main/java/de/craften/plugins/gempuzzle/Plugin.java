package de.craften.plugins.gempuzzle;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * A plugin that adds gem puzzles (also known as 15 puzzles), using item frames.
 */
public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ItemFrameListener(this), this);
    }
}

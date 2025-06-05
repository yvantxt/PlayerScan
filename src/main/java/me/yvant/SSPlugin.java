package me.yvant;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class SSPlugin extends JavaPlugin {

    private static SSPlugin instance;
    private final HashMap<UUID, Location> savedLocations = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("ss").setExecutor(new SSCommand());
        getCommand("screenshare").setExecutor(new SSCommand());
        getCommand("unss").setExecutor(new SSCommand());
        getCommand("unscreenshare").setExecutor(new SSCommand());
        getCommand("setss").setExecutor(new SSCommand());
        getCommand("setscreenshare").setExecutor(new SSCommand());
    }

    public static SSPlugin getInstance() {
        return instance;
    }

    public void savePlayerLocation(Player player) {
        savedLocations.put(player.getUniqueId(), player.getLocation());
    }

    public Location getSavedLocation(Player player) {
        return savedLocations.get(player.getUniqueId());
    }

    public void removeSavedLocation(Player player) {
        savedLocations.remove(player.getUniqueId());
    }
}

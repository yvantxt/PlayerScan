package me.yvant;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SSCommand implements CommandExecutor {

    private final SSPlugin plugin = SSPlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
            return true;
        }

        Player player = (Player) sender;

        switch (label.toLowerCase()) {
            case "ss":
            case "screenshare":
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GOLD + "=== Screenshare Plugin by yvant ===");
                    player.sendMessage(ChatColor.YELLOW + "/ss <player> - Teleport for SS");
                    player.sendMessage(ChatColor.YELLOW + "/unss <player> - Return from SS");
                    player.sendMessage(ChatColor.YELLOW + "/setss - Set SS Location");
                    player.sendMessage(ChatColor.YELLOW + "discord: trolleryvant | telegram: yvant_dev");
                    return true;
                }

                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    player.sendMessage(ChatColor.YELLOW + "Plugin reloaded. (by yvant)");
                    return true;
                }

                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        plugin.savePlayerLocation(target);
                        World world = Bukkit.getWorld(config.getString("ss-location.world"));
                        double x = config.getDouble("ss-location.x");
                        double y = config.getDouble("ss-location.y");
                        double z = config.getDouble("ss-location.z");
                        target.teleport(new Location(world, x, y, z));

                        if (config.getBoolean("messages.title.enabled")) {
                            target.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                    config.getString("messages.title.text")), "", 10, 40, 10);
                        }

                        if (config.getBoolean("messages.chat.enabled")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    config.getString("messages.chat.text")));
                        }

                        if (config.getBoolean("messages.sound.enabled")) {
                            target.playSound(target.getLocation(),
                                    Sound.valueOf(config.getString("messages.sound.ss")), 1, 1);
                        }

                        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " sent to SS.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                    }
                }
                break;

            case "unss":
            case "unscreenshare":
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        if (config.getBoolean("return-to-spawn")) {
                            target.teleport(target.getWorld().getSpawnLocation());
                        } else {
                            Location back = plugin.getSavedLocation(target);
                            if (back != null) {
                                target.teleport(back);
                            }
                        }

                        if (config.getBoolean("messages.sound.enabled")) {
                            target.playSound(target.getLocation(),
                                    Sound.valueOf(config.getString("messages.sound.unss")), 1, 1);
                        }

                        target.sendMessage(ChatColor.GREEN + "You have been released from SS.");
                        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " returned.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                    }
                }
                break;

            case "setss":
            case "setscreenshare":
                Location loc = player.getLocation();
                config.set("ss-location.world", loc.getWorld().getName());
                config.set("ss-location.x", loc.getX());
                config.set("ss-location.y", loc.getY());
                config.set("ss-location.z", loc.getZ());
                plugin.saveConfig();
                player.sendMessage(ChatColor.GREEN + "SS location has been set.");
                break;
        }

        return true;
    }
}

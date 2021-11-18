package me.imbuzz.dev.dropnotify;

import me.imbuzz.dev.dropnotify.files.ConfigFile;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropNotify extends JavaPlugin implements Listener, CommandExecutor {

    private ConfigFile configFile;

    @Override
    public void onEnable() {
        configFile = new ConfigFile(this);

        getCommand("notifyreload").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("DropNotify Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DropNotify Disabled!");
    }

    @EventHandler
    public void onItemGet(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                configFile.getData().getString("message.dropMessage")
                        .replace("%number%", event.getItem().getItemStack().getAmount() + "")
                        .replace("%item%", WordUtils.capitalize(event.getItem().getItemStack().getType().toString().toLowerCase().replace("_", " ")) + "")
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("dropnotify.reload")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    configFile.getData().getString("message.noPermission")
            ));
            return false;
        }
        if (command.getName().equalsIgnoreCase("notifyreload")){
            configFile.reload(this);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    configFile.getData().getString("message.reloadMessage")
            ));
        }

        return true;
    }
}

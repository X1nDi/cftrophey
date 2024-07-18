package ru.conderfix.cftrophy;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.conderfix.cftrophy.commands.CFTrophyCommands;
import ru.conderfix.cftrophy.listeners.QuitEvent;
import ru.conderfix.cftrophy.listeners.TeleportPlayer;
import ru.conderfix.cftrophy.listeners.TrophyPlace;

public final class CFTrophy extends JavaPlugin {

    public static Plugin plugin;
    public static PlayerPointsAPI ppAPI;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getCommand("trophy").setExecutor(new CFTrophyCommands());
        new InventoryCheck().startInvChecking();
        getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        getServer().getPluginManager().registerEvents(new TeleportPlayer(), this);
        getServer().getPluginManager().registerEvents(new TrophyPlace(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppAPI = PlayerPoints.getInstance().getAPI();
        }
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

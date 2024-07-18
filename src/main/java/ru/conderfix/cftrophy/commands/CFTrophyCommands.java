package ru.conderfix.cftrophy.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.conderfix.cftrophy.CFTrophy;
import ru.conderfix.cftrophy.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CFTrophyCommands implements CommandExecutor {

    public static boolean isShard(ItemStack item) {
        if (item != null && item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(
                NamespacedKey.fromString("cftropy"),
                PersistentDataType.STRING)) {
            return true;
        }
        return false;
    }
    private static final HashMap<Player, Integer> playersData = new HashMap<>();

    public static void setFounded(Player p) {
        playersData.put(p, 1);
    }

    public static int getFounded(Player p) {
        return playersData.get(p);
    }

    public static void clearFounded(Player p) {
        playersData.put(p, 0);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("give")) {
            if (sender.hasPermission("admin.barbary")) {
                final Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("Player offline.");
                    return true;
                }
                giveTrophy(target);
            }
        }
        if (args[0].equals("swap")) {
            final Player target = (Player) sender;
            clearFounded(target);
            for (ItemStack item : target.getInventory().getContents()) {
                if (isShard(item)) {
                    CFTrophy.ppAPI.give(
                            target.getUniqueId(),
                            CFTrophy.plugin.getConfig().getInt("trophy-bar") * item.getAmount());
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(Utils.hexcolor(
                                CFTrophy.plugin.getConfig().getString("messages.done-swap"))
                                .replace("{player}", target.getName().toString())
                                .replace("{bar}",
                                String.valueOf(CFTrophy.plugin.getConfig().getInt("trophy-bar") * item.getAmount())));
                            item.setAmount(0);
                            setFounded(target);
//                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                    });
                    break;
                }
            }
            if (getFounded(target) == 0) {
                target.sendMessage(Utils.hexcolor(CFTrophy.plugin.getConfig().getString("messages.no-item")));
            }
        }


        return true;
    }

    public static void giveTrophy(final Player player) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                CFTrophy.plugin.getConfig().getString("item.texture")));
        try {
            final Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        }
        catch (IllegalAccessException | NoSuchFieldException ex2) {
            ex2.printStackTrace();
        }
        skullMeta.setDisplayName(Utils.hexcolor(CFTrophy.plugin.getConfig().getString("item.name")));
        List<String> lore = CFTrophy.plugin.getConfig().getStringList("item.lore");
        List<String> translatedLore = new ArrayList<>();
        for (String line : lore) {
            String translatedLine = Utils.hexcolor(line);
            translatedLore.add(translatedLine);
        }
        skullMeta.setLore(translatedLore);
        NamespacedKey key = NamespacedKey.fromString("cftropy");
        skullMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "");
        itemStack.setItemMeta(skullMeta);
        player.getInventory().addItem(itemStack);
    }
}

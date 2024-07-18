package ru.conderfix.cftrophy.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.conderfix.cftrophy.InventoryCheck;

public class QuitEvent implements Listener {

    public static boolean isShard(ItemStack item) {
        if (item != null && item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(
                NamespacedKey.fromString("cftropy"),
                PersistentDataType.STRING)) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void on(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        for (ItemStack item : player.getInventory().getContents()) {
            if (isShard(item)) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                player.getInventory().remove(item);
                if (player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().has(
                        NamespacedKey.fromString("cftropy"),
                        PersistentDataType.STRING)) {
                    player.getInventory().setHelmet(null);
                }
            }
        }
    }
}

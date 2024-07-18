package ru.conderfix.cftrophy.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.conderfix.cftrophy.CFTrophy;
import ru.conderfix.cftrophy.InventoryCheck;
import ru.conderfix.cftrophy.Utils;

public class TeleportPlayer implements Listener {

    public static boolean isShard(ItemStack item) {
        if (item != null && item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(
                NamespacedKey.fromString("cftropy"),
                PersistentDataType.STRING)) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void onPlayerTeleport(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL) {
                for (ItemStack item : event.getPlayer().getInventory().getContents()) {
                    if (isShard(item)) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Utils.hexcolor(
                                CFTrophy.plugin.getConfig().getString("messages.no-teleport")
                        ));
                    }
                }
            }
        }
    }
}

package ru.conderfix.cftrophy.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import ru.conderfix.cftrophy.commands.CFTrophyCommands;

import static ru.conderfix.cftrophy.commands.CFTrophyCommands.giveTrophy;

public class TrophyPlace implements Listener {

    private NamespacedKey key = NamespacedKey.fromString("cftropy");

    @EventHandler
    public void onShmatokPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType() == Material.PLAYER_HEAD) {
            if (e.getItemInHand().getItemMeta().getPersistentDataContainer().has(
                    key,
                    PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDispense(BlockDispenseArmorEvent e) {
        if (e.getItem().getType() == Material.PLAYER_HEAD) {
            if (e.getItem().getItemMeta().getPersistentDataContainer().has(
                    key,
                    PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }
}

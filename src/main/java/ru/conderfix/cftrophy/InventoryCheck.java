package ru.conderfix.cftrophy;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InventoryCheck {

    private List<Player> players = new ArrayList<>();
    private BossBar bossBar = Bukkit.createBossBar(Utils.hexcolor(CFTrophy.plugin.getConfig().getString("bossbar.text")),
            BarColor.valueOf(CFTrophy.plugin.getConfig().getString("bossbar.color")),
            BarStyle.valueOf(CFTrophy.plugin.getConfig().getString("bossbar.style")));

    private void removeBossBar(Player player) {
        if (bossBar.getPlayers().contains(player)) {
            bossBar.removePlayer(player);
        }
    }

    private void removeFromPlayers(Player player) {
        players.remove(player);
    }

    public boolean isPlayerHadTrophy(Player player) {
        if (players.contains(player)) {
            return true;
        } else {
            return false;
        }
    }

    public void startInvChecking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item == null || item.getItemMeta() == null) {
                            continue;
                        }
                        if (item.getItemMeta().getPersistentDataContainer().has(
                                NamespacedKey.fromString("cftropy"),
                                PersistentDataType.STRING)) {
                            bossBar.addPlayer(p);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2,1));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 2, 1));
                            players.add(p);
                            return;
                        }
                    }
                    removeBossBar(p);
                    removeFromPlayers(p);
                }
            }
        }.runTaskTimer(CFTrophy.plugin, 0, 20);
    }
}

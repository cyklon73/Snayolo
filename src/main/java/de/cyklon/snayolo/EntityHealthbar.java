package de.cyklon.snayolo;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityHealthbar implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof Damageable damageable) {
            event.getEntity().setCustomNameVisible(true);
            event.getEntity().setCustomName(getHealthString(damageable.getHealth()));
        }
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof Damageable damageable) {
            event.getEntity().setCustomNameVisible(true);
            event.getEntity().setCustomName(getHealthString(damageable.getHealth()));
        }
    }

    public static String getHealthString(double value) {
        int fCount = (int) (value / 2);
        int hCount = (int) (value % 2);
        int eCount = 10 - fCount - hCount;
        return (ChatColor.DARK_RED + "❤").repeat(Math.max(0, fCount)) +
                (ChatColor.RED + "❤").repeat(Math.max(0, hCount)) +
                (ChatColor.GRAY + "❤").repeat(Math.max(0, eCount));
    }
}

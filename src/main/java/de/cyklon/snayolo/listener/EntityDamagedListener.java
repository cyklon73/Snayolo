package de.cyklon.snayolo.listener;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamagedListener implements Listener, Constants {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if (!instance().isStarted()) event.setCancelled(true);
        if (event.getEntity() instanceof Player player) {
            EventPlayer ep = instance().playerBoard.getPlayer(player);
            if (ep.isSpectator()) {
                event.setCancelled(true);
            }
        }
    }

}

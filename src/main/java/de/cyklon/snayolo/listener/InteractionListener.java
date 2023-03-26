package de.cyklon.snayolo.listener;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener, Constants {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        EventPlayer ep = instance().playerBoard.getPlayer(player);
        if (ep.isSpectator()) {
            event.setCancelled(true);
        }
    }

}

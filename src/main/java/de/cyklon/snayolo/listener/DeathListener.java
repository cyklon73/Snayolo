package de.cyklon.snayolo.listener;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener, Constants {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!instance().isStarted()) return;
        EventPlayer ep = instance().playerBoard.getPlayer(player);
        ep.setSpectator(true);
    }
}

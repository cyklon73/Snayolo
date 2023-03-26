package de.cyklon.snayolo.listener;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.Snayolo;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener, Constants {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerBoard().join(player).setSpectator(instance().isStarted());
        if (!instance().isStarted()) player.setGameMode(GameMode.ADVENTURE);
        else player.teleport(eventWorld().getSpawnLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Snayolo.getInstance().playerBoard.leave(event.getPlayer());
    }

}

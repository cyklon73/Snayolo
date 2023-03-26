package de.cyklon.snayolo.listener;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener, Constants {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        EventPlayer ep = instance().playerBoard.getPlayer(player);
        if (ep.isSpectator() || (!instance().isStarted() && !player.getGameMode().equals(GameMode.CREATIVE))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        EventPlayer ep = instance().playerBoard.getPlayer(player);
        if (ep.isSpectator() || (!instance().isStarted() && !player.getGameMode().equals(GameMode.CREATIVE))) {
            event.setCancelled(true);
        }
    }
}

package de.cyklon.snayolo.util;

import de.cyklon.snayolo.game.PlayerBoard;
import org.bukkit.entity.Player;

public interface EventPlayer {

    Player getPlayer();

    PlayerBoard.PlayerState getState();

    EventPlayer setState(PlayerBoard.PlayerState state);

    int getSubState();

    EventPlayer setSubState(int subState);

    EventPlayer setSpectator(boolean spectator);

    boolean isSpectator();
}

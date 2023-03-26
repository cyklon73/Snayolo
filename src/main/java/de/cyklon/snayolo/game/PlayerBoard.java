package de.cyklon.snayolo.game;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import io.github.cyklon73.cytils.utils.Pair;
import io.github.cyklon73.cytils.utils.Time;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class PlayerBoard implements Constants {

    private final HashMap<UUID, Pair<Player, PlayerState>> players;

    public PlayerBoard() {
        this(Collections.emptyList());
    }

    public PlayerBoard(Collection<Player> players) {
        this.players = new LinkedHashMap<>();
        players.forEach(this::join);
    }

    public EventPlayer join(Player player) {
        return join(player, PlayerState.LIVING);
    }

    public EventPlayer join(Player player, PlayerState state) {
        players.put(player.getUniqueId(), new Pair<>(player, state));
        return getPlayer(player);
    }

    public void leave(Player player) {
        leave(player.getUniqueId());
    }

    public void leave(UUID uuid) {
        players.remove(uuid);
    }

    public EventPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public EventPlayer getPlayer(UUID uuid) {
        Pair<Player, PlayerState> pair = players.get(uuid);
        if (pair==null) return null;
        return new EventPlayer() {
            @Override
            public Player getPlayer() {
                return pair.getFirst();
            }

            @Override
            public PlayerState getState() {
                return pair.getSecond();
            }

            @Override
            public EventPlayer setState(PlayerState state) {
                PlayerBoard.this.setState(this.getPlayer().getUniqueId(), state);
                return this;
            }

            @Override
            public int getSubState() {
                return this.getState().getSubState();
            }

            @Override
            public EventPlayer setSubState(int subState) {
                this.setState(this.getState().sub(subState));
                return this;
            }

            @Override
            public EventPlayer setSpectator(boolean spectator) {
                setState(getState().sub(spectator ? 1 : 0));
                Player player = getPlayer();
                player.setGameMode(spectator ? GameMode.ADVENTURE : GameMode.SURVIVAL);
                Time.sleep(10);
                player.setAllowFlight(spectator);
                player.setInvisible(spectator);
                player.setInvulnerable(spectator);
                player.setCanPickupItems(!spectator);
                player.getInventory().clear();
                player.setPlayerListName((spectator ? ChatColor.GRAY : "") + player.getDisplayName());
                return this;
            }

            @Override
            public boolean isSpectator() {
                return getState().getSubState()==1;
            }
        };
    }

    public void setState(UUID uuid, PlayerState state) {
        Pair<Player, PlayerState> pair = players.get(uuid);
        if (pair==null) return;
        pair.setSecond(state);
        players.replace(uuid, pair);
    }

    public Collection<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        players.forEach((k, v) -> list.add(v.getFirst()));
        return list;
    }

    public Collection<Player> getDeathPlayers() {
        List<Player> list = new ArrayList<>();
        players.forEach((k, v) -> {
            if (v.getSecond().equals(PlayerState.DEATH)) list.add(v.getFirst());
        });
        return list;
    }

    public Collection<Player> getLivingPlayers() {
        Collection<Player> collection = getPlayers();
        collection.removeAll(getDeathPlayers());
        return collection;
    }

    public int getPlayerCount() {
        return getPlayers().size();
    }

    public int getDeathPlayerCount() {
        return getDeathPlayers().size();
    }

    public int getLivingPlayerCount() {
        return getLivingPlayers().size();
    }

    public static enum PlayerState {
        LIVING,
        DEATH;

        private int subState = SubState.NONE;

        PlayerState() {

        }

        public int getSubState() {
            return subState;
        }

        public PlayerState sub(int subState) {
            this.subState = subState;
            return this;
        }

        public static interface SubState {
            @State public static final int NONE = 0;
            @State() public static final int SPECTATING = 1;

            public static String parseState(int state) {
                Field[] fields = SubState.class.getFields();
                for (Field field : fields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.isAnnotationPresent(State.class) && field.getType().equals(int.class)) {
                        try {
                            int value = field.getInt(null);
                            if (value == state) {
                                String name = field.getAnnotation(State.class).name().strip();
                                return name.equals("") ? field.getName() : name;
                            }
                        } catch (IllegalAccessException ignored) {}
                    }
                }
                return "NONE";
            }

            @Retention(RetentionPolicy.RUNTIME)
            @Target(ElementType.FIELD)
            @interface State {
                String name() default "";
            }
        }
    }

}

package de.cyklon.snayolo.util;

import de.cyklon.snayolo.game.PlayerBoard;
import de.cyklon.snayolo.Snayolo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface Constants {

    default Snayolo instance() {
        return Snayolo.getInstance();
    }

    default PlayerBoard playerBoard() {
        return instance().playerBoard;
    }

    default PluginManager pluginManager() {
        return Bukkit.getPluginManager();
    }

    default List<Plugin> plugins() {
        return Arrays.asList(pluginManager().getPlugins());
    }

    default List<Permission> permissions(Plugin plugin) {
        return plugin.getDescription().getPermissions();
    }

    default World eventWorld() {
        return Bukkit.getWorld(UUID.fromString("ccb47895-c4d0-4fa2-b272-95d29c2a9222"));
    }

    default World lobbyWorld() {
        return Bukkit.getWorld(UUID.fromString("059e22ac-178d-4951-8a7e-3e9c7522a24f"));
    }

}

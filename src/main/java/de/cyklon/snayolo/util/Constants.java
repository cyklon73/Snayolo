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
        return Bukkit.getWorld(UUID.fromString("1aaec0d4-b2ad-4efa-a808-2de11d3ba717"));
    }

    default World lobbyWorld() {
        return Bukkit.getWorld(UUID.fromString("059e22ac-178d-4951-8a7e-3e9c7522a24f"));
    }

}

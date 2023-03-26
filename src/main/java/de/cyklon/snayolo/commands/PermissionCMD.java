package de.cyklon.snayolo.commands;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.TabTemplates;
import io.github.cyklon73.cytils.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PermissionCMD implements CommandExecutor, TabCompleter, TabTemplates, Constants {

    private final static String[] DEFAULT_PLUGINS = new String[] {"minecraft", "bukkit", "spigot"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) return false;
        Player target = null;
        if (args.length == 3) target = Bukkit.getPlayer(args[2]);
        if (target==null && sender instanceof Player player) target = player;
        if (target==null) {
            sender.sendMessage(ChatColor.RED + "Target not found/valid!");
            return true;
        }

        String[] argsId = args[0].split(":");
        String pluginName = argsId[0];
        String permission = argsId[1];

        if (Util.arrayContains(DEFAULT_PLUGINS, pluginName)) {
            target.addAttachment(instance(), permission, Boolean.parseBoolean(args[1]));
            sender.sendMessage(ChatColor.GREEN + "Set Permission '" + args[0] + "' on " + target.getName() + " to " + Boolean.parseBoolean(args[1]));
        } else {
            Plugin plugin = pluginManager().getPlugin(pluginName);

            if (plugin!=null) {
                target.addAttachment(plugin, permission, Boolean.parseBoolean(args[1]));
                sender.sendMessage(ChatColor.GREEN + "Set Permission '" + args[0] + "' on " + target.getName() + " to " + Boolean.parseBoolean(args[1]));
            } else {
                sender.sendMessage(ChatColor.RED + "Plugin '" + pluginName + "' not found!");
                return true;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        switch (args.length) {
            case 0 -> {
                return list;
            }
            case 1 -> {
                for (Plugin plugin : plugins()) {
                    List<Permission> permissions = permissions(plugin);
                    for (Permission permission : permissions) {
                        list.add(String.format("%s:%s", plugin.getName().toLowerCase(), permission.getName().toLowerCase()));
                    }
                }
                for (Permission perm : Bukkit.getServer().getPluginManager().getDefaultPermissions(true)) {
                    String pl = perm.getName().split("\\.")[0];
                    if (Util.arrayContains(DEFAULT_PLUGINS, pl.strip())) list.add(String.format("%s:%s", pl, perm.getName()));
                }
            }
            case 2 -> listBooleans(list);
            case 3 -> listOnlinePlayers(list);
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));

        return list;
    }
}

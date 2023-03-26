package de.cyklon.snayolo.commands;

import de.cyklon.snayolo.util.Constants;
import io.github.cyklon73.cytils.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EventCMD implements CommandExecutor, TabCompleter, Constants {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("start")) {
                if (instance().isStarted()) {
                    player.sendMessage(ChatColor.RED + "Already started!");
                    return false;
                }
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    if (!p.getWorld().equals(eventWorld())) p.teleport(eventWorld().getSpawnLocation());
                });
                instance().start();
                player.sendMessage(ChatColor.GREEN + "Started successfully");
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (instance().isStarted()) {
                    instance().end();
                    sender.sendMessage(ChatColor.GREEN + "Event successfully ended!");
                }
                sender.sendMessage(ChatColor.RED + "Event isn`t running now!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                list.add("start");
                list.add("stop");
            }
            default -> {
                return list;
            }
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));

        return list;
    }
}

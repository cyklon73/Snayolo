package de.cyklon.snayolo.commands;

import de.cyklon.snayolo.util.ChatFormatter;
import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.Util;
import io.github.cyklon73.cytils.bukkit.generator.chunk.EmptyChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldCMD implements CommandExecutor, TabCompleter, Constants {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /world tp/register <worldname>");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            String worldName = args[1];
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                player.sendMessage(ChatColor.RED + "World not found: " + worldName);
                return true;
            }
            player.sendMessage(ChatFormatter.format(String.format("""
                    name: '%s'
                    uuid: '%s'
                    difficulty: '%s'
                    gametime: %s.0
                    pvp: %s
                    """, world.getName(), world.getUID(), world.getDifficulty(), world.getGameTime(), world.getPVP()), ChatColor.WHITE, true, true, true));
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /world tp <worldname>");
        }

        if (args[0].equalsIgnoreCase("register")) {
            String worldName = args[1];
            if (Bukkit.getWorld(worldName)!=null) {
                player.sendMessage(ChatColor.RED + "World with same name found: " + worldName);
                return true;
            }

            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.generateStructures(false);
            worldCreator.generator(new EmptyChunkGenerator());
            World world = instance().getServer().createWorld(worldCreator);
            instance().getServer().getWorlds().add(world);

            YamlConfiguration yamlConfiguration = instance().getWorldConfiguration();
            yamlConfiguration.set("worlds." + worldName + ".generator", worldCreator.generator().getClass().getName());
            try {
                instance().saveWorldConfiguration(yamlConfiguration);
            } catch (IOException e) {
                e.printStackTrace();
            }

            sender.sendMessage(ChatFormatter.format("World '" + worldName + "' has been registered.", ChatColor.GREEN, true, true, true));
        }

        if (args[0].equalsIgnoreCase("tp")) {
            String worldName = args[1];
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                player.sendMessage(ChatColor.RED + "World not found: " + worldName);
                return true;
            }
            player.teleport(world.getSpawnLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to world: " + worldName);
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
                list.add("tp");
                list.add("info");
                list.add("register");
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "tp", "info" -> {
                        for (World world : Bukkit.getWorlds()) {
                            list.add(world.getName());
                        }
                    }
                }
            }
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));

        return list;
    }
}

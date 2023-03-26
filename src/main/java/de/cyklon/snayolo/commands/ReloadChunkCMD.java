package de.cyklon.snayolo.commands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadChunkCMD implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Überprüfe, ob der Befehl von einem Spieler ausgeführt wird
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Location location = player.getLocation();

        Chunk chunk = location.getChunk();

        location.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());

        player.sendMessage("Alle Light-Level im Chunk wurden neu berechnet.");

        return true;
    }

}

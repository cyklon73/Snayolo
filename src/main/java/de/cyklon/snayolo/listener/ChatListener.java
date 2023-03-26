package de.cyklon.snayolo.listener;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        String message = String.format("<%s> %s", event.getPlayer().getDisplayName(), event.getMessage());
        String dateString = dateFormat.format(date);
        TextComponent text = new TextComponent(message);
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(dateString).create()));

        Bukkit.getOnlinePlayers().forEach((p) -> p.spigot().sendMessage(text));
        event.setCancelled(true);
    }
}

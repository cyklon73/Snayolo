package de.cyklon.snayolo.util;

import org.bukkit.Bukkit;

import java.util.ArrayList;

public interface TabTemplates {

    default void listBooleans(ArrayList<String> list) {
        list.add("true");
        list.add("false");
    }

    default void listOnlinePlayers(ArrayList<String> list) {
        Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
    }

}

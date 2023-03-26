package de.cyklon.snayolo.util;

import org.bukkit.ChatColor;

public class ChatFormatter {

    public static String format(String message, ChatColor mainColor, boolean formatInQuotes, boolean formatBoolean, boolean formatNumbers) {
        StringBuilder sb = new StringBuilder();
        for (String line : message.split("\n")) {
            sb.append(format_(line, mainColor, formatInQuotes, formatBoolean, formatNumbers)).append("\n");
        }
        sb = new StringBuilder(sb.substring(0, sb.length()-2));
        return sb.toString();
    }

    private static String format_(String message, ChatColor mainColor, boolean formatInQuotes, boolean formatBoolean, boolean formatNumbers) {
        StringBuilder result = new StringBuilder();
        String[] words = message.split(" ");
        for (String word : words) {
            if (word.startsWith("\"") && word.endsWith("\"") || word.startsWith("'") && word.endsWith("'") || word.startsWith("`") && word.endsWith("`")) {
                if (formatInQuotes) {
                    result.append(ChatColor.GREEN).append(word).append(" ");
                } else {
                    result.append(word).append(" ");
                }
            } else if (word.startsWith("(") && word.endsWith(")") || word.startsWith("[") && word.endsWith("]") ||
                    word.startsWith("{") && word.endsWith("}")) {
                result.append(ChatColor.LIGHT_PURPLE).append(word).append(" ");
            } else if (formatBoolean && (word.equalsIgnoreCase("true") || word.equalsIgnoreCase("false"))) {
                result.append(ChatColor.GOLD).append(word).append(" ");
            } else {
                try {
                    Double.parseDouble(word);
                    if (formatNumbers) {
                        int commaIndex = word.indexOf(".");
                        if (commaIndex > -1) {
                            result.append(ChatColor.AQUA).append(word.substring(0, commaIndex)).append(ChatColor.DARK_GRAY)
                                    .append(".").append(ChatColor.AQUA).append(word.substring(commaIndex + 1)).append(" ");
                        } else {
                            result.append(mainColor).append(word).append(" ");
                        }
                    } else {
                        result.append(word).append(" ");
                    }
                } catch (NumberFormatException e) {
                    if (formatBoolean && (word.equalsIgnoreCase("true") || word.equalsIgnoreCase("false"))) {
                        result.append(ChatColor.GOLD).append(word).append(" ");
                    } else {
                        result.append(mainColor).append(word).append(" ");
                    }
                }
            }
        }
        return result.toString();
    }




}

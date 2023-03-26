package de.cyklon.snayolo.util;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Util {

    public static String combine(String[] array, String separator) {
        return combine(array, 0, array.length-1, separator);
    }

    public static String combine(String[] array, int start, int end, String separator) {
        if (array == null || start < 0 || start >= array.length || end < start || end >= array.length) {
            throw new IllegalArgumentException("Invalid array or indexes");
        }
        StringBuilder result = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i > start) {
                result.append(separator);
            }
            result.append(array[i]);
        }
        return result.toString();
    }

    public static ItemStack[] asStack(Material... materials) {
        return asStack(1, materials);
    }

    public static ItemStack[] asStack(int amount, Material... materials) {
        ItemStack[] stacks = new ItemStack[materials.length];
        for (int i = 0; i < stacks.length; i++) {
            Material mat = materials[i];
            stacks[i] = mat==null ? null : new ItemStack(mat, amount);
        }
        return stacks;
    }

    public static double distance(double num1, double num2) {
        return Math.max(num1, num2) - Math.min(num1, num2);
    }

    public static class Arrays {
        public static <T> T[] flip(T[] array) {
            ArrayUtils.reverse(array);
            return array;
        }
    }
    public static class Location {

        public static org.bukkit.Location[] radius(Player player, int radius) {
            return radius(player.getLocation(), radius);
        }

        public static org.bukkit.Location[] radius(org.bukkit.Location center, int radius) {
            org.bukkit.Location[] locations = new org.bukkit.Location[(int) Math.pow((2 * radius) + 1, 3)];

            int i = 0;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        locations[i++] = center.clone().add(x, y, z);
                    }
                }
            }

            return locations;
        }
    }


}

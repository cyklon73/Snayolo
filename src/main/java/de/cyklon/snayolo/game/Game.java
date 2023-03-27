package de.cyklon.snayolo.game;

import de.cyklon.snayolo.util.Constants;
import de.cyklon.snayolo.util.EventPlayer;
import de.cyklon.snayolo.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static de.cyklon.snayolo.util.Util.asStack;


public class Game implements Constants {

    private final static HashMap<UUID, List<Zombie>> zombies = new HashMap<>();

    public final BossBar waveDisplay;

    public int state = -1;
    public int waves = 0;
    private final int totalWaves = 8;

    private final ItemStack[][] armor;

    public Game() {
        this.waveDisplay = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        this.waveDisplay.setProgress(0);

        this.armor = Init.ARMOR();
    }

    public void start() {
        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.setLevel(0);
            p.setExp(0);
            p.getInventory().clear();
            p.getActivePotionEffects().forEach((effect) -> p.removePotionEffect(effect.getType()));
        });
        startCountdown();
    }

    public void onDeath(EventPlayer ep) {
        ep.setSpectator(true);
        if (instance().playerBoard.getLivingPlayerCount()==0) {
            instance().end();
            //TODO ep win as last standing player
        }
    }

    private void updateWaveDisplay() {
        this.waveDisplay.setTitle(String.format("%sWaves %s%s%s/%s%s", ChatColor.YELLOW, ChatColor.AQUA, waves, ChatColor.DARK_GRAY, ChatColor.AQUA, totalWaves));
    }

    public void startCountdown() {
        state = 0;
        Bukkit.getOnlinePlayers().forEach(waveDisplay::addPlayer);
        new BukkitRunnable() {
            int seconds = 5;

            @Override
            public void run() {
                if (instance().isEnded()) cancel();
                if (seconds > 0) {
                    Bukkit.getOnlinePlayers().forEach((p) -> p.sendTitle(ChatColor.GREEN + "" + seconds, "", 0, 20, 0));
                    seconds--;
                } else {
                    cancel();
                    gameCountdown();
                }
            }
        }.runTaskTimer(instance(), 0, 20);
    }

    private void gameCountdown() {
        state = 1;
        updateWaveDisplay();
        Bukkit.getOnlinePlayers().forEach((p) -> p.setGameMode(GameMode.SURVIVAL));
        new BukkitRunnable() {
            //int seconds = 10 * 60;
            int seconds = 10;

            @Override
            public void run() {
                if (instance().isEnded()) cancel();
                if (seconds > 0) {
                    String minutes = String.format("%dm %02ds", seconds / 60, seconds % 60);
                    Bukkit.getOnlinePlayers().forEach((p) -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.BLUE + "Nächste Welle: " + ChatColor.GRAY + minutes)));
                    seconds--;
                } else {
                    waves++;
                    if (waves>totalWaves) {
                        Collection<Player> winners = instance().playerBoard.getLivingPlayers();
                        instance().end();
                        winners.forEach((p) -> {

                        });
                        cancel();
                    } else {
                        //seconds = 10 * 60;
                        seconds = 10;
                        updateWaveDisplay();
                        Bukkit.getOnlinePlayers().forEach((p) -> {
                            int radius = 30;
                            //Location[] locations = Util.Location.radius(p, radius);
                            int amount = switch (waves) {
                                case 1, 2, 3, 4, 5, 6 -> 5;
                                case 7, 8 -> 10;
                                default -> 0;
                            };
                            double damage = switch (waves) {
                                case 1 -> 1.5;
                                case 2 -> 3;
                                case 3, 7 -> 5;
                                case 4, 5 -> 2;
                                case 6 -> 4;
                                case 8 -> 6.5;
                                default -> 0;
                            };
                            Zombie[] zombies = new Zombie[amount];
                            for (int i = 0; i < zombies.length; i++) {
                                //zombies[i] = spawnZombie(Random.randomChoice(locations), damage, armor[waves-1]);
                                zombies[i] = spawnZombieAroundPlayer(p, radius, damage, armor[waves - 1]);
                            }
                            addZombie(p, zombies);
                        });
                    }
                }
            }
        }.runTaskTimer(instance(), 0, 20);
    }
    private Zombie spawnZombie(Location location, double damage, ItemStack[] armor) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setRemoveWhenFarAway(false);
        zombie.getEquipment().setItemInMainHand(armor[4]);
        zombie.getEquipment().setArmorContents(Util.Arrays.flip(Arrays.copyOf(armor, armor.length-1)));
        zombie.setCustomNameVisible(false);
        AttributeInstance attribute = zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        attribute.setBaseValue(damage);
        return zombie;
    }

    public Zombie spawnZombieAroundPlayer(Player player, int radius, double damage, ItemStack[] armor) {
        Location playerLoc = player.getLocation();
        World world = playerLoc.getWorld();
        if (world==null) return null;
        double angle = Math.random() * Math.PI * 2;
        double x = 0;
        double y = -9999;
        double z = 0;
        int c = 0;
        while (y<player.getLocation().getY()-5) {
            if (c==10) {
                radius+=5;
                c=0;
            }
            x = playerLoc.getX() + radius * Math.cos(angle);
            z = playerLoc.getZ() + radius * Math.sin(angle);
            y = getGroundLevel(world, x, playerLoc.getY()+3, z);
            c++;
        }
        Location loc = new Location(world, x, y, z);
        Zombie zombie = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
        zombie.setRemoveWhenFarAway(false);
        EntityEquipment ee = zombie.getEquipment();
        if (ee!=null) {
            ee.setItemInMainHand(armor[4]);
            ee.setArmorContents(Util.Arrays.flip(Arrays.copyOf(armor, armor.length-1)));
        }
        zombie.setCustomNameVisible(false);
        AttributeInstance ai = zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (ai!=null) ai.setBaseValue(damage);
        zombie.setTarget(player);
        return zombie;
    }

    private double getGroundLevel(World world, double x, double y, double z) {
        Location loc = new Location(world, x, y, z);
        while (loc.getBlock().getType() == Material.AIR && loc.getY() > 0) {
            loc.setY(loc.getY() - 1);
        }
        return loc.getY();
    }

    public void destroyDisplay() {
        Bukkit.getScheduler().runTask(instance(), waveDisplay::removeAll);
    }

    private static class Init {
        public static ItemStack[][] ARMOR() {
            ItemStack[][] armor = new ItemStack[8][5];
            for (int w = 0; w < armor.length; w++) {
                armor[w] = switch (w+1) {
                    case 4 -> asStack(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, null);
                    case 5 -> asStack(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_SWORD);
                    case 6, 7, 8 -> asStack(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, null);
                    default -> new ItemStack[] {null, null, null, null, null};
                };
            }
            return armor;
        }
    }

    private static void addZombie(Player player, Zombie... zos) {
        List<Zombie> zo = zombies.get(player.getUniqueId());
        if (zo==null) zo = new ArrayList<>();
        zo.addAll(Arrays.asList(zos));
        zombies.put(player.getUniqueId(), zo);
    }
}

package de.cyklon.snayolo;

import de.cyklon.snayolo.commands.EventCMD;
import de.cyklon.snayolo.commands.PermissionCMD;
import de.cyklon.snayolo.commands.ReloadChunkCMD;
import de.cyklon.snayolo.commands.WorldCMD;
import de.cyklon.snayolo.game.Game;
import de.cyklon.snayolo.game.PlayerBoard;
import de.cyklon.snayolo.listener.*;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Snayolo extends JavaPlugin {

    public final String prefix = String.format("%s[%sSnayolo%s] %s", ChatColor.DARK_GRAY, ChatColor.YELLOW, ChatColor.DARK_GRAY, ChatColor.RESET);

    private static Snayolo INSTANCE;

    private boolean started = false;

    public final Game game = new Game();
    public final PlayerBoard playerBoard = new PlayerBoard();

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();


        registerEvent(new BlockListener());
        registerEvent(new ConnectionListener());
        registerEvent(new DeathListener());
        registerEvent(new EntityDamagedListener());
        registerEvent(new FoodLevelListener());
        registerEvent(new ChatListener());
        registerEvent(new InteractionListener());
        //registerEvent(new EntityHealthbar());

        registerCommand("permission", new PermissionCMD());
        registerCommand("world", new WorldCMD());
        registerCommand("event", new EventCMD());
        registerCommand("reload-chunk", new ReloadChunkCMD());

        Bukkit.getOnlinePlayers().forEach((p) -> playerBoard.join(p).setSpectator(false).getPlayer().setGameMode(GameMode.ADVENTURE));

        try {
            getLogger().info(prefix + "Initialize worlds...");
            YamlConfiguration config = getWorldConfiguration();

            ConfigurationSection worldsSection = config.getConfigurationSection("worlds");

            if (worldsSection != null) {
                for (String worldName : worldsSection.getKeys(false)) {
                    try {
                        getLogger().info(prefix + "Initialize world '" + worldName + "'");
                        String generatorName = worldsSection.getString(worldName + ".generator");

                        ClassLoader classLoader = getClass().getClassLoader();
                        Class<?> generatorClass = classLoader.loadClass(generatorName.strip());
                        ChunkGenerator generator = (ChunkGenerator) generatorClass.getDeclaredConstructor().newInstance();

                        WorldCreator worldCreator = new WorldCreator(worldName);
                        worldCreator.generateStructures(false);
                        worldCreator.generator(generator);
                        World world = getServer().createWorld(worldCreator);
                        getServer().getWorlds().add(world);
                        getLogger().info(prefix + ChatColor.GREEN + "Successfully Initialized world '" + worldName + "'");
                    } catch (Exception e) {
                        getLogger().info(prefix + ChatColor.RED + "Failed Initialize world '" + worldName + "'");
                        e.printStackTrace();
                    }
                }
            }
            getLogger().info(prefix + ChatColor.GREEN + "Initialize worlds done!");
        } catch (Exception e) {
            getLogger().info(prefix + ChatColor.RED + "Failed Initialize worlds!");
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        this.game.destroyDisplay();
    }

    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }

    public void start() {
        game.start();
        this.started = true;
    }

    public void end() {
        this.started = false;
        this.game.destroyDisplay();
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isEnded() {
        return !isStarted();
    }

    public static Snayolo getInstance() {
        return INSTANCE;
    }

    public YamlConfiguration getWorldConfiguration() {
        File file = new File(getDataFolder(), "//worlds.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveWorldConfiguration(YamlConfiguration yamlConfiguration) throws IOException {
        File file = new File(getDataFolder() + "//worlds.yml");
        yamlConfiguration.save(file);
    }
}

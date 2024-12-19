package tr.alperendemir.antiAFK;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    // Configuration settings
    public long maxAFKTime;
    public int maxClicksPerSecond;
    public boolean enableWaterLoopDetection;
    public boolean enableAutoClickerDetection;
    public String kickMessageAFK;
    public String kickMessageWaterLoop;
    public String kickMessageAutoClicker;
    public int waterLoopMovementThreshold;
    public long waterLoopTimeThreshold;
    public long autoClickerTimeThreshold;
    public long autoClickerWarningInterval;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        maxAFKTime = config.getLong("max-afk-seconds", 300) * 1000; // Convert seconds to milliseconds
        maxClicksPerSecond = config.getInt("max-clicks-per-second", 10);
        enableWaterLoopDetection = config.getBoolean("enable-water-loop-detection", true);
        enableAutoClickerDetection = config.getBoolean("enable-auto-clicker-detection", true);
        kickMessageAFK = colorize(config.getString("kick-message-afk", "&cYou have been kicked for being AFK."));
        kickMessageWaterLoop = colorize(config.getString("kick-message-water-loop", "&cYou have been kicked for using a water loop."));
        kickMessageAutoClicker = colorize(config.getString("kick-message-auto-clicker", "&cYou have been kicked for using an auto-clicker."));
        waterLoopMovementThreshold = config.getInt("water-loop-movement-threshold", 20);
        waterLoopTimeThreshold = config.getLong("water-loop-time-threshold", 300) * 1000; // 5 minutes
        autoClickerTimeThreshold = config.getLong("auto-clicker-time-threshold", 300) * 1000; // 5 minutes
        autoClickerWarningInterval = config.getLong("auto-clicker-warning-interval", 30) * 1000; // 30 seconds
    }

    private String colorize(String message) {
        return message.replace("&", "§");
    }
}

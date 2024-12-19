package tr.alperendemir.antiAFK;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AntiAFK extends JavaPlugin {

    private ConfigManager configManager;
    private Map<UUID, Long> playerActivityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Initialize configuration
        configManager = new ConfigManager(this);

        // Register detection classes
        getServer().getPluginManager().registerEvents(new WaterLoopDetector(configManager), this);
        getServer().getPluginManager().registerEvents(new AutoClickerDetector(configManager), this);

        // Register general activity listeners
        getServer().getPluginManager().registerEvents(new GeneralActivityListener(this), this);

        // Start the AFK check task
        new AFKCheckTask().runTaskTimer(this, 20L, 20L);

        // Register commands
        this.getCommand("antiafk").setExecutor(new AntiAFKCommandExecutor(this));
    }

    // Called by activity listeners to update player activity
    public void updatePlayerActivity(Player player) {
        playerActivityMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void onDisable() {
        // Cleanup
        playerActivityMap.clear();
    }

    private class AFKCheckTask extends BukkitRunnable {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player != null && (player.hasPermission("antiafk.bypass") || player.isOp())) {
                    continue; // Skip this player if they have the bypass permission or are an operator
                }

                UUID playerId = player.getUniqueId();
                long lastActivityTime = playerActivityMap.getOrDefault(playerId, currentTime);

                if (currentTime - lastActivityTime > configManager.maxAFKTime) {
                    // Kick the player for being AFK
                    player.kickPlayer(configManager.kickMessageAFK);

                    // Reset activity data for the kicked player
                    playerActivityMap.remove(playerId);
                }
            }
        }
    }
}

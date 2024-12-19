package tr.alperendemir.antiAFK;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WaterLoopDetector implements Listener {
    private final ConfigManager configManager;
    private final Map<UUID, List<Location>> waterMovementHistory = new HashMap<>();
    private final Map<UUID, Long> waterLoopStartTime = new HashMap<>();

    public WaterLoopDetector(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if water loop detection is enabled
        if (!configManager.enableWaterLoopDetection) return;

        // Bypass check for operators or players with bypass permission
        if (player.hasPermission("antiafk.bypass") || player.isOp()) return;

        // Check if the player is in water
        if (player.getLocation().getBlock().getType() == Material.WATER) {
            List<Location> locations = waterMovementHistory.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
            locations.add(player.getLocation());

            // Limit the list to the last X movements
            if (locations.size() > configManager.waterLoopMovementThreshold) {
                locations.remove(0);
            }

            // Check if the player is moving in a small area
            if (locations.size() == configManager.waterLoopMovementThreshold && isSmallArea(locations)) {
                waterLoopStartTime.putIfAbsent(player.getUniqueId(), System.currentTimeMillis());
                long timeInArea = System.currentTimeMillis() - waterLoopStartTime.get(player.getUniqueId());

                // Warn the player when half of the time threshold is met
                if (timeInArea >= configManager.waterLoopTimeThreshold / 2) {
                    player.sendMessage("§cYou are at risk of being kicked for using a water loop.");
                }

                // Kick the player if the time threshold is met
                if (timeInArea >= configManager.waterLoopTimeThreshold) {
                    player.kickPlayer(configManager.kickMessageWaterLoop);
                    waterMovementHistory.remove(player.getUniqueId());
                    waterLoopStartTime.remove(player.getUniqueId());
                }
            } else {
                // Reset the timer if the player moves out of the small area
                waterLoopStartTime.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        waterMovementHistory.remove(playerId);
        waterLoopStartTime.remove(playerId);
    }

    private boolean isSmallArea(List<Location> locations) {
        if (locations.size() < configManager.waterLoopMovementThreshold) return false;

        // Calculate the bounding box of the locations
        double minX = locations.get(0).getX();
        double maxX = locations.get(0).getX();
        double minZ = locations.get(0).getZ();
        double maxZ = locations.get(0).getZ();

        for (Location loc : locations) {
            minX = Math.min(minX, loc.getX());
            maxX = Math.max(maxX, loc.getX());
            minZ = Math.min(minZ, loc.getZ());
            maxZ = Math.max(maxZ, loc.getZ());
        }

        // Check if the area is 10x10 or smaller
        return (maxX - minX) <= 10 && (maxZ - minZ) <= 10;
    }
}

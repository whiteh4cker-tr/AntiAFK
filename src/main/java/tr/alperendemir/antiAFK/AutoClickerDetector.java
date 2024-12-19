package tr.alperendemir.antiAFK;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class AutoClickerDetector implements Listener {
    private final ConfigManager configManager;
    private final Map<UUID, List<Long>> clickIntervals = new HashMap<>();
    private final Map<UUID, Long> lastClickTime = new HashMap<>();
    private final Map<UUID, Long> warningSentTime = new HashMap<>(); // Tracks when the warning was sent

    public AutoClickerDetector(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            trackClick(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            trackClick((Player) event.getDamager());
        }
    }

    private void trackClick(Player player) {

        // Bypass check for operators or players with bypass permission
        if (player.hasPermission("antiafk.bypass") || player.isOp()) return;

        if (!configManager.enableAutoClickerDetection) return;

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Record the time interval between the last click and this click
        if (lastClickTime.containsKey(playerId)) {
            long interval = currentTime - lastClickTime.get(playerId);
            List<Long> intervals = clickIntervals.computeIfAbsent(playerId, k -> new ArrayList<>());
            intervals.add(interval);

            // Limit the list to the last 20 intervals
            if (intervals.size() > 20) {
                intervals.remove(0);
            }

            // Analyze the intervals for auto-clicker behavior
            if (intervals.size() == 20 && isConsistentPattern(intervals)) {
                // Check if the warning has been sent
                if (!warningSentTime.containsKey(playerId) || (currentTime - warningSentTime.get(playerId)) >= configManager.autoClickerWarningInterval) {
                    player.sendMessage("§cYou are at risk of being kicked for using an auto-clicker.");
                    warningSentTime.put(playerId, currentTime);
                }

                // Kick the player if the time threshold is met
                if (currentTime - lastClickTime.get(playerId) >= configManager.autoClickerTimeThreshold) {
                    player.kickPlayer(configManager.kickMessageAutoClicker);
                    clickIntervals.remove(playerId);
                    lastClickTime.remove(playerId);
                    warningSentTime.remove(playerId);
                }
            } else {
                // Reset the warning timer if the pattern is not consistent
                warningSentTime.remove(playerId);
            }
        }

        // Update the last click time
        lastClickTime.put(playerId, currentTime);
    }

    private boolean isConsistentPattern(List<Long> intervals) {
        if (intervals.size() < 20) return false;

        // Calculate the average interval
        long total = 0;
        for (long interval : intervals) {
            total += interval;
        }
        long average = total / intervals.size();

        // Check if all intervals are within a small range of the average
        long maxAllowedDifference = 5; // Maximum allowed difference in milliseconds
        for (long interval : intervals) {
            if (Math.abs(interval - average) > maxAllowedDifference) {
                return false;
            }
        }

        return true;
    }
}

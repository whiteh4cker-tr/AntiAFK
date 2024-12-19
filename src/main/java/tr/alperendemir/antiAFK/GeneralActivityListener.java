package tr.alperendemir.antiAFK;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class GeneralActivityListener implements Listener {
    private final AntiAFK plugin;

    public GeneralActivityListener(AntiAFK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().equals(event.getTo())) { // Ignore negligible movements
            plugin.updatePlayerActivity(event.getPlayer());
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.updatePlayerActivity(event.getPlayer());
    }
}
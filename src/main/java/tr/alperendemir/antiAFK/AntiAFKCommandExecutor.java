package tr.alperendemir.antiAFK;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiAFKCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public AntiAFKCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("antiafk")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("antiafk.admin")) {
                    plugin.reloadConfig();
                    sender.sendMessage("§aAntiAFK configuration reloaded!");
                    return true;
                } else {
                    sender.sendMessage("§cYou do not have permission to use this command.");
                    return true;
                }
            }
        }
        return false;
    }
}

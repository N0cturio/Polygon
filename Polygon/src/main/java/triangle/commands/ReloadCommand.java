package triangle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import triangle.Polygon;

public class ReloadCommand implements CommandExecutor {

    private final Polygon plugin;

    public ReloadCommand(Polygon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("polygon.command.reload")) {
            plugin.reloadConfig();
            sender.sendMessage("Polygon config reloaded.");
            return true;
        } else {
            sender.sendMessage("You do not have permission to reload the config.");
            return false;
        }
    }
}

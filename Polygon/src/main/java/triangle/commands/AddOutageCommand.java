package triangle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import triangle.Polygon;
import triangle.managers.SpaceshipManager;

public class AddOutageCommand implements CommandExecutor {

    private final Polygon plugin;

    public AddOutageCommand(Polygon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }

        String outageName = args[0];
        int experienceNeeded;
        try {
            experienceNeeded = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid experience value.");
            return false;
        }

        String startCommand = args[2];

        SpaceshipManager spaceshipManager = plugin.getSpaceshipManager();
        spaceshipManager.addOutage(outageName, experienceNeeded, startCommand);

        sender.sendMessage("Outage " + outageName + " added with " + experienceNeeded + " experience needed.");
        return true;
    }
}

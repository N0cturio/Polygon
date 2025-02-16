package triangle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import triangle.Polygon;

import java.util.Random;

public class RollCommand implements CommandExecutor {

    private final Polygon plugin;

    public RollCommand(Polygon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("Usage: /roll <max>");
                return false;
            }

            try {
                int max = Integer.parseInt(args[0]);
                Random random = new Random();
                int rollResult = random.nextInt(max + 1);
                player.sendMessage("You rolled a " + rollResult);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid number format.");
            }
        }
        return true;
    }
}

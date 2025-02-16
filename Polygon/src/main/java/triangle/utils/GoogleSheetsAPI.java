package triangle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import triangle.Polygon;
import triangle.managers.SpaceshipManager;

import java.util.HashMap;
import java.util.Map;

public class OutageFixCommand implements CommandExecutor {

    private final Polygon plugin;
    private final Map<Player, Long> lastUsed = new HashMap<>();  // Store player last used time

    public OutageFixCommand(Polygon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            SpaceshipManager spaceshipManager = plugin.getSpaceshipManager();
            int playerExperience = spaceshipManager.getPlayerExperience(player);

            // Check cooldown
            long currentTime = System.currentTimeMillis();
            long lastTime = lastUsed.getOrDefault(player, 0L);
            long cooldownTime = plugin.getConfig().getLong("cooldown-time") * 1000L;  // Configurable cooldown time

            if (currentTime - lastTime < cooldownTime) {
                long timeLeft = (cooldownTime - (currentTime - lastTime)) / 1000;
                player.sendMessage("You need to wait " + timeLeft + " seconds before using this command again.");
                return false;
            }

            // Set new cooldown timestamp
            lastUsed.put(player, currentTime);

            // Proceed with outage fixing logic
            if (playerExperience < 10) {
                player.sendMessage("You don't have enough experience to fix this outage.");
                return false;
            }

            // Cooldown logic based on player level
            int cooldown = calculateCooldown(playerExperience);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("You can now fix the outage again.");
                }
            }.runTaskLater(plugin, cooldown * 20L); // Delay in ticks (1 tick = 1/20 second)

            player.sendMessage("Fixing the outage...");

            return true;
        }
        return false;
    }

    private int calculateCooldown(int playerExperience) {
        // Cooldown based on level
        if (playerExperience < 20) {
            return 160; // 8 minutes for level 1
        } else if (playerExperience < 40) {
            return 120; // 6 minutes for level 2
        } else {
            return 80; // 4 minutes for level 3
        }
    }
}

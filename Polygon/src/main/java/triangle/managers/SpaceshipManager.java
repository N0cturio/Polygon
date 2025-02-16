package triangle.managers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import triangle.Polygon;
import triangle.utils.GoogleSheetsAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SpaceshipManager implements Listener {

    private final Polygon plugin;
    private final GoogleSheetsAPI googleSheetsAPI;

    private final Map<String, Integer> outagesExperienceNeeded = new HashMap<>();
    private final Map<String, String[]> outagesStartCommands = new HashMap<>();
    private final Map<String, String[]> outagesFixCommands = new HashMap<>();

    public SpaceshipManager(Polygon plugin) {
        this.plugin = plugin;
        this.googleSheetsAPI = new GoogleSheetsAPI();
        initializeOutages();
    }

    private void initializeOutages() {
        // Example "door_blocked" outage
        outagesExperienceNeeded.put("door_blocked", 5);
        outagesStartCommands.put("door_blocked", new String[] { "/bigdoor 24 lock", "/broadcast Outage in Sector B - Door 24" });
        outagesFixCommands.put("door_blocked", new String[] { "/bigdoor unlock" });
    }

    public void addOutage(String outageName, int experienceNeeded, String startCommand) {
        outagesExperienceNeeded.put(outageName, experienceNeeded);
        outagesStartCommands.put(outageName, new String[]{ startCommand });
        outagesFixCommands.put(outageName, new String[]{ "/bigdoor unlock" }); // Default fix command, can be extended
    }

    public void startRandomOutage() {
        Random rand = new Random();
        String[] outages = {"door_blocked"};
        String outage = outages[rand.nextInt(outages.length)];

        // Execute start commands for the outage
        for (String command : outagesStartCommands.get(outage)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        }
    }

    public void fixOutage(String outageName, Player player) {
        int playerExperience = getPlayerExperience(player); // Corrected method call
        
        if (outagesFixCommands.containsKey(outageName) && playerExperience >= outagesExperienceNeeded.get(outageName)) {
            for (String command : outagesFixCommands.get(outageName)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
            }
        } else {
            player.sendMessage("You do not have enough experience to fix this outage!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DIAMOND_BLOCK) {
            Player player = event.getPlayer();
            startRandomOutage();
            player.sendMessage("A random outage has been triggered!");
        }
    }

    public Map<String, Integer> getOutagesExperienceNeeded() {
        return outagesExperienceNeeded;
    }

    public int getPlayerExperience(Player player) {
        try {
            return googleSheetsAPI.getPlayerExperience(player.getName());  // Retrieves player experience from Google Sheets
        } catch (Exception e) {
            player.sendMessage("Error retrieving experience data.");
            return 0;
        }
    }
}

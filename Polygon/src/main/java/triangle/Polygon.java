package triangle;

import org.bukkit.plugin.java.JavaPlugin;
import triangle.commands.RollCommand;
import triangle.commands.OutageFixCommand;
import triangle.commands.ReloadCommand;
import triangle.commands.AddOutageCommand;
import triangle.managers.SpaceshipManager;

public class Polygon extends JavaPlugin {

    private SpaceshipManager spaceshipManager;

    @Override
    public void onEnable() {
        spaceshipManager = new SpaceshipManager(this);
        getServer().getPluginManager().registerEvents(spaceshipManager, this);

        // Register commands
        getCommand("roll").setExecutor(new RollCommand(this));
        getCommand("outagefix").setExecutor(new OutageFixCommand(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("addoutage").setExecutor(new AddOutageCommand(this));

        // Load plugin config
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SpaceshipManager getSpaceshipManager() {
        return spaceshipManager;
    }
}

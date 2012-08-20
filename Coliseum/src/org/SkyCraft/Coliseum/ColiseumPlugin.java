package org.SkyCraft.Coliseum;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Listeners.BlockListener;
import org.SkyCraft.Coliseum.Listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ColiseumPlugin extends JavaPlugin {

	private Logger log;
	private Set<Arena> arenaSet = new HashSet<Arena>();
	private ConfigHandler confHandler;
	//private SQLHandler sqlHandler;
	private ColiseumCommandExecutor executor;
	private PlayerListener pListener;
	private BlockListener bListener;
	
	public void onEnable() {
        log = getLogger();
        arenaSet = new HashSet<Arena>();
        confHandler = new ConfigHandler(this, log);
		//SQLHandler sqlHandler = new SQLHandler("user", "password");
		getServer().getPluginManager().registerEvents(pListener, this);
		getServer().getPluginManager().registerEvents(bListener, this);
		executor = new ColiseumCommandExecutor(this, log, confHandler);
		getCommand("coliseum").setExecutor(executor);
        pListener = new PlayerListener(this);
        bListener = new BlockListener(this);
        
		confHandler.loadArenas();
	}
	
	public Set<Arena> getArenaSet() {
		return arenaSet;
	}

	public ConfigHandler getConfigHandler() {
		return confHandler;
	}
	
	public PlayerListener getPlayerListener() {
		return pListener;
	}

}

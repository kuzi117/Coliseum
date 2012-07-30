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
	private Set<String> playerAlreadyJoined = new HashSet<String>();
	private ConfigHandler confHandler;
	private ColiseumCommandExecutor executor;
	private PlayerListener pListener;
	private BlockListener bListener;
	
	public void onEnable() {
		pListener = new PlayerListener(this);
		bListener = new BlockListener(this);
		getServer().getPluginManager().registerEvents(pListener, this);
		getServer().getPluginManager().registerEvents(bListener, this);
		
		log = getLogger();
		confHandler = new ConfigHandler(this, log);
		getCommand("coliseum").setExecutor(executor = new ColiseumCommandExecutor(this, log, confHandler));
		confHandler.loadArenas();
	}
	
	public Set<Arena> getArenaSet() {
		return arenaSet;
	}
	
	public boolean isPlayerJoined(String name) {
		return playerAlreadyJoined.contains(name);
	}
	
	public void joinPlayer(String name) {
		playerAlreadyJoined.add(name);
		return;
	}
	
	public void leavePlayer(String name) {
		playerAlreadyJoined.remove(name);
		return;
	}

	public ConfigHandler getConfigHandler() {
		return confHandler;
	}
	
	public PlayerListener getPlayerListener() {
		return pListener;
	}

}

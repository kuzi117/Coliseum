package org.SkyCraft.Coliseum;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Listeners.BlockListener;
import org.SkyCraft.Coliseum.Listeners.DamageListener;
import org.SkyCraft.Coliseum.Listeners.DeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ColiseumPlugin extends JavaPlugin {

	private Logger log;
	private Set<Arena> arenaSet = new HashSet<Arena>();
	private Set<String> playerAlreadyJoined = new HashSet<String>();
	private ConfigHandler confHandler;
	private ColiseumCommandExecutor executor;
	private DeathListener dListener;
	private DamageListener daListener;
	private BlockListener bListener;
	//TODO Need logout listener to remove and move player on logout
	
	public void onEnable() {
		log = getLogger();
		confHandler = new ConfigHandler(this, log);
		getCommand("coliseum").setExecutor(executor = new ColiseumCommandExecutor(this, log, confHandler));
		confHandler.loadArenas();
		dListener = new DeathListener(this);
		daListener = new DamageListener(this);
		bListener = new BlockListener(this);
		getServer().getPluginManager().registerEvents(dListener, this);
		getServer().getPluginManager().registerEvents(daListener, this);
		getServer().getPluginManager().registerEvents(bListener, this);
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

}

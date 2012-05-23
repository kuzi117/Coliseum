package org.SkyCraft.Coliseum;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.bukkit.plugin.java.JavaPlugin;

public class ColiseumPlugin extends JavaPlugin {

	private ColiseumCommandExecutor executor;
	private Logger log = Logger.getLogger("Minecraft");
	private Set<Arena> arenaSet = new HashSet<Arena>();
	private Set<String> playerAlreadyJoined = new HashSet<String>();
	
	
	public void onEnable() {
		getCommand("coliseum").setExecutor(executor = new ColiseumCommandExecutor(this, log));
	}
	
	public Set<Arena> getArenaSet() {
		log.info("poop");
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

}

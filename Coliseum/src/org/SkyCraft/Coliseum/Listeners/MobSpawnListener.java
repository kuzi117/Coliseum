package org.SkyCraft.Coliseum.Listeners;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Arena;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnListener implements Listener {
	
	ColiseumPlugin plugin;
	
	MobSpawnListener(ColiseumPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleMobSpawn(CreatureSpawnEvent e) {
		Location loc = e.getLocation();
		for(Arena arena : plugin.getArenaSet()) {
			if(arena.getRegion().isBlockContained(loc)) {
				e.setCancelled(true);
				break;
			}
		}
		return;
	}
	
	

}

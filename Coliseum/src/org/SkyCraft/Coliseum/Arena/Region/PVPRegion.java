package org.SkyCraft.Coliseum.Arena.Region;

import java.util.HashMap;
import org.bukkit.Location;

public class PVPRegion extends Region {

	private HashMap<String, Location> teamSpawns;
	
	public PVPRegion() {
		teamSpawns = new HashMap<String, Location>();
	}
	
	public void addTeamSpawn(String name, Location loc) {
		teamSpawns.put(name, loc);
		return;
	}
	
	public void removeTeamSpawn(String name) {
		teamSpawns.remove(name);
	}
	
	public Location getTeamSpawn(String name) {
		return teamSpawns.get(name);
	}
	
	public boolean isCompleteRegion(int teamSize) {
		if(teamSpawns.size() != teamSize) {
			return false;
		}
		return super.isCompleteRegion();
	}
}

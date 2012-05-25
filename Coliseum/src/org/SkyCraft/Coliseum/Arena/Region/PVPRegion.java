package org.SkyCraft.Coliseum.Arena.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

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
}

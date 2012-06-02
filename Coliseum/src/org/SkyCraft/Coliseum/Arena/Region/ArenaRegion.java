package org.SkyCraft.Coliseum.Arena.Region;

import java.util.HashMap;

import org.SkyCraft.Coliseum.Arena.Region.Region;

import org.bukkit.Location;

public abstract class ArenaRegion extends Region {
	
	private HashMap<String, Location> teamSpawns;
	
	ArenaRegion() {
		teamSpawns = new HashMap<String, Location>();
	}

	public boolean addTeamSpawn(String name, Location loc) {
		if(isBlockContained(loc)) {
			teamSpawns.put(name, loc.add(.5, 1, .5));
			return true;
		}
		return false;
	}
	
	public void removeTeamSpawn(String name) {
		teamSpawns.remove(name);
	}
	
	public Location getTeamSpawn(String name) {
		return teamSpawns.get(name);
	}
	
	protected boolean isCompleteRegion(int teamSize) {
		if(teamSpawns.size() != teamSize) {
			return false;
		}
		return super.isCompleteRegion();
	}
}

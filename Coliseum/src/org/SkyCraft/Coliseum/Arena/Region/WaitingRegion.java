package org.SkyCraft.Coliseum.Arena.Region;

import org.bukkit.Location;

public class WaitingRegion extends Region {

	private Location waitingSpawn;
	
	WaitingRegion() {}

	public void setSpawn(Location waitingSpawn) {
		this.waitingSpawn = waitingSpawn;
	}

	public Location getSpawn() {
		return waitingSpawn;
	}
}

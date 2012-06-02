package org.SkyCraft.Coliseum.Arena.Region;

import org.bukkit.Location;

public class WaitingRegion extends Region {

	private Location waitingSpawn;
	
	public WaitingRegion() {}

	public boolean setSpawn(Location waitingSpawn) {
		if(isBlockContained(waitingSpawn)) {
			this.waitingSpawn = waitingSpawn.add(.5, 1, .5);
			return true;
		}
		return false;
	}

	public Location getSpawn() {
		return waitingSpawn;
	}

	public boolean isCompleteRegion() {
		if(waitingSpawn == null) {
			return false;
		}
		return super.isCompleteRegion();
	}
}

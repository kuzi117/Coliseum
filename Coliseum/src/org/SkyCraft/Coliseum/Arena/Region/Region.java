package org.SkyCraft.Coliseum.Arena.Region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class Region {

	private Location loc1;
	private Location loc2;
	
	//TODO implement checks to ensure no "flat" regions
	
	Region() {}
	
	public void setPos1(Block block) {
		if(loc1 == null) {
			loc1 = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
		}
		else {
			loc1.setX(block.getX());
			loc1.setY(block.getY());
			loc1.setZ(block.getZ());
			loc1.setWorld(block.getWorld());
		}
	}

	public void setPos1(double x, double y, double z, World world) {
		if(loc1 == null) {
			loc1 = new Location(world, x, y, z);
		}
		else {
			loc1.setX(x);
			loc1.setY(y);
			loc1.setZ(z);
			loc1.setWorld(world);
		}
	}
	
	public void setPos2(Block block) {
		if(loc2 == null) {
			loc2 = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
		}
		else {
			loc2.setX(block.getX());
			loc2.setY(block.getY());
			loc2.setZ(block.getZ());
			loc2.setWorld(block.getWorld());
		}
	}
	
	public void setPos2(double x, double y, double z, World world) {
		if(loc2 == null) {
			loc2 = new Location(world, x, y, z);
		}
		else {
			loc2.setX(x);
			loc2.setY(y);
			loc2.setZ(z);
			loc2.setWorld(world);
		}
	}
	
	public Location getPos1() {
		return loc1;
	}
	
	public Location getPos2() {
		return loc2;
	}
	
	protected boolean isCompleteRegion() {
		if(loc1 == null || loc2 == null || !loc1.getWorld().equals(loc2.getWorld())) {
			return false;
		}
		return true;
	}
	
	public boolean isBlockContained(Location loc) {
		if(isBetween(loc1.getBlockX(), loc2.getBlockX(), loc.getBlockX()) && 
				isYBetween(loc1.getBlockY(), loc2.getBlockY(), loc.getBlockY()) && 
				isBetween(loc1.getBlockZ(), loc2.getBlockZ(), loc.getBlockZ()) &&
				loc.getWorld().equals(loc1.getWorld()) && loc.getWorld().equals(loc2.getWorld())) {
			return true;
		}
		
		return false;
	}

	private boolean isBetween(int c1, int c2, int cB) {
		if(c1 > c2) {
			if(cB < c1 && cB > c2) {
				return true;
			}
		}
		else {
			if(cB < c2 && cB > c1) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isYBetween(int c1, int c2, int cB) {
		if(c1 > c2) {
			if(cB < c1 && cB > c2 - 1) {
				return true;
			}
		}
		else {
			if(cB < c2 && cB > c1 - 1) {
				return true;
			}
		}
		return false;
	}
}

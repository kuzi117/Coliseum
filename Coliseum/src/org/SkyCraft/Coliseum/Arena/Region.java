package org.SkyCraft.Coliseum.Arena;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Region {

	Vector pos1;
	Vector pos2;
	
	//TODO implement checks to ensure no "flat" regions
	
	Region() {}
	
	Region(Vector impPos, int posNum) {
		if (posNum == 1) {
			pos1 = impPos;
		}
		else if (posNum == 2) {
			pos2 = impPos;
		}
	}
	
	Region(Vector impPos1, Vector impPos2) {
			pos1 = impPos1;
			pos2 = impPos2;
	}
	
	void setPos1(Block block) {
		pos1.setX(block.getX());
		pos1.setY(block.getY());
		pos1.setZ(block.getZ());
	}

	void setPos1(int x, int y, int z) {
		pos1.setX(x);
		pos1.setY(y);
		pos1.setZ(z);
	}
	
	void setPos2(Block block) {
		pos2.setX(block.getX());
		pos2.setY(block.getY());
		pos2.setZ(block.getZ());
	}
	
	void setPos2(int x, int y, int z) {
		pos2.setX(x);
		pos2.setY(y);
		pos2.setZ(z);
	}
	
	boolean isCompleteRegion() {
		if (pos1 != null && pos2 != null) {
			return true;
		}
		return false;
	}
	
	public boolean isBlockContained(Location loc) {
		if(isBetween(pos1.getBlockX(), pos2.getBlockX(), loc.getBlockX()) && 
				isBetween(pos1.getBlockY(), pos2.getBlockY(), loc.getBlockY()) && 
				isBetween(pos1.getBlockZ(), pos2.getBlockZ(), loc.getBlockZ())) {
			return true;
		}
		
		return false;
	}

	private boolean isBetween(int c1, int c2, int cB) {
		if (c1 > c2) {
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
}

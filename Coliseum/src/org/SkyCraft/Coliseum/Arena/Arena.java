package org.SkyCraft.Coliseum.Arena;

import org.bukkit.entity.Player;

public abstract class Arena {
	private String arenaName;
	//TODO Implement players list/array/something
	
	Arena(String arenaName) {
		this.arenaName = arenaName;
	}
	
	public abstract Region getRegion();
	
	public boolean isThisArena(String name) {
		if(arenaName.equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}
	
	public abstract boolean hasThisPlayer(Player player);
	

	public void addPlayer(Player player) {
		//combatants.add(player.getName());  TODO ADD COMBATANT INIT
		
	}
	
	public void removePlayer(Player player) {
		//TODO REMOVE COMBATANT
	}
	
}

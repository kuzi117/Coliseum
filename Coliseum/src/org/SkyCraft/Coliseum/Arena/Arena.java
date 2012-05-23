package org.SkyCraft.Coliseum.Arena;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Arena {
	private String arenaName;
	private Region arenaRegion;
	private Set<Combatant> combatants;
	//TODO Implement players list/array/something
	
	Arena(String arenaName) {
		this.arenaName = arenaName;
		arenaRegion = new Region();
		combatants = new HashSet<Combatant>();
	}
	
	public Region getRegion() {
		return arenaRegion;
	}
	
	public boolean isThisArena(String name) {
		if(arenaName.equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}
	
	public boolean hasThisPlayer(Player player) {
		for(Combatant combatant : combatants) {
			if (combatant.getPlayer().equals(player)) {
				return true;
			}
		}
		return false;
	}
	

	public void addPlayer(Player player) {
		//combatants.add(player.getName());  TODO ADD COMBATANT INIT
		
	}
	
	public void removePlayer(Player player) {
		//TODO REMOVE COMBATANT
	}
	
}

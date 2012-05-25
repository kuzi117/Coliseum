package org.SkyCraft.Coliseum.Arena;

import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Region.Region;
import org.bukkit.entity.Player;

public abstract class Arena {
	private String arenaName;
	private Set<Player> editors;
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
	
	public boolean isPlayerEditing(Player player) {
		if(editors.contains(player)) {
			return true;
		}
		return false;
	}

	public void setPlayerEditing(Player editor) {
		editors.add(editor);
		return;
	}
	
	public abstract boolean hasThisPlayer(Player player);

	public abstract void addPlayer(Player player);
	
	public abstract void removePlayer(Player player);
	
	public abstract void start();
	
}

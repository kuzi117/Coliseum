package org.SkyCraft.Coliseum.Arena;

import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Region.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Arena {
	private String arenaName;
	protected Set<Player> editors;
	private boolean enabled;
	
	Arena(String arenaName) {
		this.arenaName = arenaName;
		enabled = false;
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
		if(enabled) {
			editor.sendMessage(ChatColor.GRAY + "[Coliseum] The arena you chose was enabled; you could not be placed in editing mode.");
			return;
		}
		editors.add(editor);
		return;
	}

	public void removeEditor(Player editor) {
		editors.remove(editor);
		return;
	}
	
	public boolean enable() {
		if(!editors.isEmpty()) {
			for(Player p : editors) {
				p.sendMessage(ChatColor.GRAY + "[Coliseum] The arena you were editing has been enabled. You are no longer editing an arena.");
			}
			editors.clear();
		}
		return enabled = true;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return arenaName;
	}
	
	public abstract boolean hasThisPlayer(Player player);

	public abstract void addPlayer(Player player);
	
	public abstract void removePlayer(Player player);
	
	public abstract void start();
	
}

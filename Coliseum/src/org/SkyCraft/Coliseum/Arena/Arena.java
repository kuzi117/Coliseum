package org.SkyCraft.Coliseum.Arena;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Combatant.Combatant;
import org.SkyCraft.Coliseum.Arena.Region.ArenaRegion;
import org.SkyCraft.Coliseum.Arena.Region.WaitingRegion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Arena {
	protected Set<Player> editors;
	protected WaitingRegion waitingRegion;
	protected HashMap<String, Integer> teams;
	private String arenaName;
	protected boolean enabled;
	protected boolean started;
	private int maxPoints = 10; //TODO CHANGE
	protected String winners;
	
	Arena(String arenaName) {
		editors = new HashSet<Player>();
		waitingRegion = new WaitingRegion();
		this.arenaName = arenaName;
		enabled = false;
		teams = new HashMap<String, Integer>();
	}
	
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

	public void removeEditor(Player editor) {
		editors.remove(editor);
		return;
	}
	
	public void addTeamName(String name) {
		teams.put(name, 0);
		return;
	}
	
	public HashMap<String, Integer> getTeams() {
		return teams;
	}
	
	public WaitingRegion getWaitingRegion() {
		return waitingRegion;
	}
	
	protected String findWinningTeam() {
		int i = 0;
		
		for(String t : teams.keySet()) {
			if(i < teams.get(t)) {
				i = teams.get(t);
				winners = t;
			}
		}
		return winners;
	}
	
	public boolean enable() {
		if(!waitingRegion.isCompleteRegion()) {
			return false;
		}
		if(!editors.isEmpty()) {
			for(Player p : editors) {
				p.sendMessage(ChatColor.GRAY + "[Coliseum] The arena you were editing has been enabled. You are no longer editing an arena.");
			}
			editors.clear();
		}
		enabled = true;
		return true;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return arenaName;
	}

	public void incrementTeamPoints(String team) {
		int i = teams.get(team);
		i++;
		if(i >= maxPoints) {
			end();
			return;
		}
		teams.put(team, i);
		return;
	}

	public void decrementTeamPoints(String team) {//TODO Perhaps allow setting of maxNegScore? Irrelevant for CTF though.
		int i = teams.get(team);
		if(i <= 0) {
			i--;
			teams.put(team, i);
		}
		return;
	}

	public abstract boolean hasThisPlayer(Player player);

	public abstract void addPlayer(Player player);
	
	public abstract void removePlayer(Player player);
	
	public abstract Combatant getCombatant(Player player);
	
	public abstract ArenaRegion getRegion();
	
	public abstract boolean start();
	
	protected abstract void end();

	public abstract void broadcastScore();
	
}

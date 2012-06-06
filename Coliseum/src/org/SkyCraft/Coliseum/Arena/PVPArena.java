package org.SkyCraft.Coliseum.Arena;

import java.util.HashSet;
import java.util.Set;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Combatant.PVPCombatant;
import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PVPArena extends Arena {
	private Set<PVPCombatant> combatants;
	private PVPRegion arenaRegion;

	public PVPArena(String arenaName, ColiseumPlugin plugin) {
		super(arenaName, plugin);
		arenaRegion = new PVPRegion();
		combatants = new HashSet<PVPCombatant>();
	}

	public PVPRegion getRegion() {
		return arenaRegion;
	}
	
	public boolean hasThisPlayer(Player player) {
		for(PVPCombatant combatant : combatants) {
			if (combatant.getPlayer().equals(player)) {
				return true;
			}
		}
		return false;
	}

	public void addPlayer(Player player) {
		PVPCombatant combatant = new PVPCombatant(player);
		combatants.add(combatant);
		combatant.toWaitingArea(waitingRegion);
	}

	public void removePlayer(Player player) {
		for(PVPCombatant combatant: combatants) {
			if(combatant.getPlayer().equals(player)) {
				combatants.remove(combatant);
				combatant.returnToLoc();
				return;
			}
		}
	}
	
	public PVPCombatant getCombatant(Player p) {
		for(PVPCombatant c : combatants) {
			if(c.getPlayer().equals(p)) {
				return c;
			}
		}
		return null;
	}

	public boolean enable() {
		if(!arenaRegion.isCompleteRegion(teams.size())) {
			return false;
		}
		return super.enable();
	}

	public boolean start() {
		if(enabled) {
			for(PVPCombatant c : combatants) {
				if(!c.isReady() || !teams.containsKey(c.getTeam())) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		for(PVPCombatant c : combatants) {
			c.toTeamSpawn(arenaRegion);
		}
		
		return started = true;
	}

	protected void end() {
		started = false;
		for(PVPCombatant c : combatants) {
			c.returnToLoc();
			plugin.leavePlayer(c.getPlayer().getName());
		}
		combatants.clear();
		for(String team : teams.keySet()) {
			teams.put(team, 0);
		}
		
	}

	public void broadcastKill(Player dead, Player killer) {
		for(PVPCombatant c : combatants) {
			Player p = c.getPlayer();
			if(!p.equals(dead) && !p.equals(killer)) {
				p.sendMessage(ChatColor.RED + killer.getName() + ChatColor.GRAY + " killed " + ChatColor.RED + dead.getName() + ChatColor.GRAY + "!");
			}
		}
		dead.sendMessage(ChatColor.GRAY + "[Coliseum] You were killed by " + ChatColor.RED + killer.getName() + ChatColor.GRAY +"!");
		killer.sendMessage(ChatColor.GRAY + "[Coliseum] You killed " + ChatColor.RED + dead.getName() + ChatColor.GRAY +"!");
	}

	public void broadcastScore() {
		for(PVPCombatant c : combatants) {
			c.getPlayer().sendMessage(ChatColor.GRAY + "The score is:");
			for(String team : teams.keySet()) {
				c.getPlayer().sendMessage(ChatColor.GRAY + team + ": " + ChatColor.GOLD + teams.get(team));
			}
		}
	}

	public void broadcastSuicide(Player dead) {
		for(PVPCombatant c : combatants) {
			Player p = c.getPlayer();
			if(!p.equals(dead)) {
				p.sendMessage(ChatColor.RED + dead.getName() + ChatColor.GRAY + " has committed suicide!");
			}
		}
		dead.sendMessage(ChatColor.GRAY + "[Coliseum] You committed suicide!");
		
	}
}

package org.SkyCraft.Coliseum.Arena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Combatant.PVPCombatant;
import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PVPArena extends Arena {
	private Set<PVPCombatant> combatants;
	private PVPRegion arenaRegion;

	public PVPArena(String arenaName) {
		super(arenaName);
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
		// TODO Auto-generated method stub
		
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
		List<String> messageList = new ArrayList<String>();
		messageList.add(ChatColor.GRAY + "The score is:");
		for(String team : teams.keySet()) {
			messageList.add(ChatColor.GRAY + team + ": " + ChatColor.GOLD + teams.get(team));
		}
		for(PVPCombatant c : combatants) {
			c.getPlayer().sendMessage((String[]) messageList.toArray());
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

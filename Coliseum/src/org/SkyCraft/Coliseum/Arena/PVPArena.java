package org.SkyCraft.Coliseum.Arena;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Combatant.PVPCombatant;
import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.SkyCraft.Coliseum.Arena.Region.Region;
import org.SkyCraft.Coliseum.Arena.Region.WaitingRegion;
import org.bukkit.entity.Player;

public class PVPArena extends Arena {
	private Set<PVPCombatant> combatants;
	private PVPRegion arenaRegion;
	private WaitingRegion waitingRegion;
	private HashMap<String, Integer> teams;

	public PVPArena(String arenaName) {
		super(arenaName);
		arenaRegion = new PVPRegion();
		combatants = new HashSet<PVPCombatant>();
	}

	public Region getRegion() {
		return arenaRegion;
	}
	
	public Region getWaitingRegion() {
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

	public boolean enable() {
		if(!arenaRegion.isCompleteRegion(teams.size()) || !waitingRegion.isCompleteRegion()) {
			return false;
		}
		return super.enable();
	}
	
	public void addTeamName(String name) {
		teams.put(name, 0);
		return;
	}

	public void start() {
		//MUST CHECK IF ALL PLAYERS HAVE TEAMS
		//MUST CHECK IF ALL TEAMS HAVE SPAWNS
		//MUST CHECK NO ONE IS EDITING
		
	}

}

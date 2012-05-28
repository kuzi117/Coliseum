package org.SkyCraft.Coliseum.Arena;

import java.util.HashSet;
import java.util.Set;

import org.SkyCraft.Coliseum.Arena.Combatant.PVPCombatant;
import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.bukkit.entity.Player;

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

	public boolean enable() {
		if(!arenaRegion.isCompleteRegion(teams.size())) {
			return false;
		}
		return super.enable();
	}

	public void start() {
		//MUST CHECK IF ALL PLAYERS HAVE TEAMS
		//MUST CHECK IF ALL TEAMS HAVE SPAWNS
		//MUST CHECK NO ONE IS EDITING
		
	}
}

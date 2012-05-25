package org.SkyCraft.Coliseum.Arena;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class PVPArena extends Arena {
	private Set<PVPCombatant> combatants;
	private PVPRegion arenaPVPRegion;

	PVPArena(String arenaName) {
		super(arenaName);
		arenaPVPRegion = new PVPRegion();
		combatants = new HashSet<PVPCombatant>();
	}

	public Region getRegion() {
		// TODO Auto-generated method stub
		return arenaPVPRegion;
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
		super.addPlayer(player);
		
	}

	public void removePlayer(Player player) {
		super.removePlayer(player);
		
	}

}

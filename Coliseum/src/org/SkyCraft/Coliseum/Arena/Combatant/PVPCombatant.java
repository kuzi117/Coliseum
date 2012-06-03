package org.SkyCraft.Coliseum.Arena.Combatant;

import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PVPCombatant extends Combatant {

	public PVPCombatant(Player player) {
		super(player);
	}
	
	public void joinTeam(String team) {
		this.team = team;
		return;
	}

	public void toTeamSpawn(PVPRegion arenaRegion) {
		player.teleport(arenaRegion.getTeamSpawn(team), TeleportCause.PLUGIN);
		return;
	}

}

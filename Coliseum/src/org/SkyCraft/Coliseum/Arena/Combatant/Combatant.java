package org.SkyCraft.Coliseum.Arena.Combatant;

import org.SkyCraft.Coliseum.Arena.Region.WaitingRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public abstract class Combatant {

	protected Player player;
	private Location prevLoc;
	private boolean readiness;
	protected String team;
	private boolean old;
	
	public Combatant(Player player) {
		this.player = player;
		prevLoc = player.getLocation();
		old = false;
	}

	public Player getPlayer() {
		return player;
	}

	public void toWaitingArea(WaitingRegion waitingRegion) {
		player.teleport(waitingRegion.getSpawn(), TeleportCause.PLUGIN);
		return;
	}
	
	public void toOldLoc() {
		player.teleport(prevLoc, TeleportCause.PLUGIN);
		return;
	}
	
	public Location getOldLoc() {
		return prevLoc;
	}
	
	public boolean setReadiness(boolean readiness) {
		if(readiness) {
			if(team != null) {
				this.readiness = readiness;
				return true;
			}
			else {
				return false;
			}
		}
		this.readiness = readiness;
		return true;
	}
	
	public boolean isReady() {
		return readiness;
	}
	
	public void joinTeam(String team) {
		this.team = team;
		return;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setOldCombatant(boolean old) {
		this.old = old;
		return;
	}
	
	public boolean isOldCombatant() {
		return old;
	}
}

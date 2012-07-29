package org.SkyCraft.Coliseum.Arena;

import java.util.HashSet;
import java.util.Set;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Combatant.Combatant;
import org.SkyCraft.Coliseum.Arena.Combatant.PVPCombatant;
import org.SkyCraft.Coliseum.Arena.Region.PVPRegion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

	public void addCombatant(Player player) {
		PVPCombatant combatant = new PVPCombatant(player);
		plugin.joinPlayer(player.getName());
		combatants.add(combatant);
		combatant.toWaitingArea(waitingRegion);
	}

	public void removeCombatant(Player player) {
		for(PVPCombatant combatant: combatants) {
			if(combatant.getPlayer().equals(player)) {
				plugin.leavePlayer(player.getName());
				combatants.remove(combatant);
				combatant.toOldLoc();
				return;
			}
		}
	}

	public void removeCombatant(Combatant combatant) {
		plugin.leavePlayer(combatant.getPlayer().getName());
		combatants.remove(combatant);
		combatant.toOldLoc();
		return;
	}

	public PVPCombatant getCombatant(Player p) {
		for(PVPCombatant c : combatants) {
			if(c.getPlayer().equals(p)) {
				return c;
			}
		}
		return null;
	}

	public void removeOldCombatant(Player player) {
		combatants.remove(getCombatant(player));
		return;
	}

	public boolean enable() {
		if(!arenaRegion.isCompleteRegion(teams.size())) {
			return false;
		}
		return super.enable();
	}

	public boolean disable() {
		if(!combatants.isEmpty()) {
			for(PVPCombatant c : combatants) {
				c.getPlayer().sendMessage(ChatColor.GRAY + "[Coliseum] This arena was disabled.");
			}
		}
		forceEnd();
		return super.disable();
	}

	public boolean start() {//TODO make sure teams are equal in some way.
		winners = null;
		if(enabled) {
			for(PVPCombatant c : combatants) {
				if(!c.isReady() || !teams.containsKey(c.getTeam())) {//TODO Remove debug info
					if(plugin.getServer().getPlayer("Braaedy") != null)
						plugin.getServer().getPlayer("Braaedy").sendMessage(c.getPlayer().getName() + " is not ready or else it's " +  !teams.containsKey(c.getTeam()) + " [DEBUG INFO]");
					return false;
				}
			}
		}
		else {
			return false;
		}
		for(PVPCombatant c : combatants) {
			if(!c.isOldCombatant()) {
				c.toTeamSpawn(arenaRegion);
				c.getPlayer().getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				c.getPlayer().getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
				c.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
				c.getPlayer().getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
				c.getPlayer().getInventory().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
			}
		}
		return started = true;
	}

	public boolean forceStart() {//TODO Should work.
		Set<PVPCombatant> remove = new HashSet<PVPCombatant>();
		for(PVPCombatant c : combatants) {
			if(!c.isReady()) {
				c.getPlayer().sendMessage(ChatColor.GRAY + "[Coliseum] You were kicked when the arena was forced to start because you were not ready.");
				remove.add(c);
			}
		}
		for(PVPCombatant c : remove) {
			removeCombatant(c);
		}
		return start();
	}

	public boolean end() {
		started = false;
		Set<PVPCombatant> retain = new HashSet<PVPCombatant>();
		for(PVPCombatant c : combatants) {
			if(!isPlayerDead(c.getPlayer().getName())) {
				removeCombatant(c);
			}
			else {
				c.setOldCombatant(true);
				retain.add(c);
			}
			Player p = c.getPlayer();
			if(findWinningTeam().equalsIgnoreCase(c.getTeam())) {
				p.sendMessage(ChatColor.GRAY + "[Coliseum] Congrats on winning!");
			}
			else {
				p.sendMessage(ChatColor.GRAY + "[Coliseum] Sorry about the loss, better luck next time!");
			}
		}
		combatants.retainAll(retain);
		for(String team : teams.keySet()) {
			teams.put(team, 0);
		}
		return true;
	}

	public boolean forceEnd() {
		for(PVPCombatant c : combatants) {
			c.getPlayer().sendMessage(ChatColor.GRAY + "[Coliseum] This arena was forced to end.");
		}
		return end();
	}

	public void broadcastKill(Player dead, Player killer) {
		for(PVPCombatant c : combatants) {
			if(!c.isOldCombatant()) {
				Player p = c.getPlayer();
				if(!p.equals(dead) && !p.equals(killer)) {
					p.sendMessage(ChatColor.RED + killer.getName() + ChatColor.GRAY + " killed " + ChatColor.RED + dead.getName() + ChatColor.GRAY + "!");
				}
			}
		}
		dead.sendMessage(ChatColor.GRAY + "[Coliseum] You were killed by " + ChatColor.RED + killer.getName() + ChatColor.GRAY +"!");
		killer.sendMessage(ChatColor.GRAY + "[Coliseum] You killed " + ChatColor.RED + dead.getName() + ChatColor.GRAY +"!");
		return;
	}

	public void broadcastScore() {
		for(PVPCombatant c : combatants) {
			if(!c.isOldCombatant()) {
				c.getPlayer().sendMessage(ChatColor.GRAY + "The score is:");
				for(String team : teams.keySet()) {
					c.getPlayer().sendMessage(ChatColor.GRAY + team + ": " + ChatColor.GOLD + teams.get(team));
				}
			}
		}
		return;
	}

	public void broadcastSuicide(Player dead) {
		for(PVPCombatant c : combatants) {
			Player p = c.getPlayer();
			if(!c.isOldCombatant()) {
				if(!p.equals(dead)) {
					p.sendMessage(ChatColor.RED + dead.getName() + ChatColor.GRAY + " has committed suicide!");
				}
			}
		}
		dead.sendMessage(ChatColor.GRAY + "[Coliseum] You committed suicide!");
		return;
	}

	public void broadcastLeave(Player leaver) {
		for(PVPCombatant c : combatants) {
			c.getPlayer().sendMessage(ChatColor.RED + leaver.getName() + ChatColor.GRAY + " has left the match.");
		}
		return;
	}
}

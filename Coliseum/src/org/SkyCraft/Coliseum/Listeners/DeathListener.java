package org.SkyCraft.Coliseum.Listeners;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Arena.PVPArena;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathListener implements Listener {

	private ColiseumPlugin plugin;

	DeathListener(ColiseumPlugin plugin) {
		this.plugin = plugin;
	}
	//TODO up priority
	@EventHandler
	public void handleDeath(EntityDeathEvent e) {
		LivingEntity entity = e.getEntity();
		EntityDamageEvent lastDamage = entity.getLastDamageCause();

		if (entity instanceof Player) {
			Player dead = (Player) entity;
			if(lastDamage instanceof EntityDamageByEntityEvent) {
				if (((EntityDamageByEntityEvent) lastDamage).getDamager() instanceof Player) {
					for(Arena a : plugin.getArenaSet()) {
						if(a instanceof PVPArena && a.hasThisPlayer((Player) ((EntityDamageByEntityEvent) lastDamage).getDamager())) {
							Player killer = (Player) ((EntityDamageByEntityEvent) lastDamage).getDamager();
							((PVPArena) a).broadcastKill(dead, killer);
							a.broadcastScore();
							a.incrementTeamPoints(a.getCombatant(killer).getTeam());
							return;
						}
					}
				}
				else if (((EntityDamageByEntityEvent) lastDamage).getDamager() instanceof Projectile) {
					if (((Projectile) ((EntityDamageByEntityEvent) lastDamage).getDamager()).getShooter() instanceof Player) {
						for(Arena a : plugin.getArenaSet()) {
							if(a instanceof PVPArena && a.hasThisPlayer((Player) ((Projectile) ((EntityDamageByEntityEvent) lastDamage).getDamager()).getShooter())) {
								Player killer = (Player) ((Projectile) ((EntityDamageByEntityEvent) lastDamage).getDamager()).getShooter();
								((PVPArena) a).broadcastKill(dead, killer);
								a.broadcastScore();
								a.incrementTeamPoints(a.getCombatant(killer).getTeam());
								return;
							}
						}
					}
				}
				else if (((EntityDamageByEntityEvent) lastDamage).getDamager() instanceof Tameable) {
					if (((Tameable) ((EntityDamageByEntityEvent) lastDamage).getDamager()).isTamed()) {
						for(Arena a : plugin.getArenaSet()) {
							if(a instanceof PVPArena && a.hasThisPlayer((Player) ((Tameable) ((EntityDamageByEntityEvent) lastDamage).getDamager()).getOwner())) {
								Player killer = (Player) ((Tameable) ((EntityDamageByEntityEvent) lastDamage).getDamager()).getOwner();
								((PVPArena) a).broadcastKill(dead, killer);
								a.broadcastScore();
								a.incrementTeamPoints(a.getCombatant(killer).getTeam());
								return;
							}
						}
					}
				}
			}
			else {
				DamageCause cause = lastDamage.getCause();
				if(cause.equals(DamageCause.CONTACT) || cause.equals(DamageCause.DROWNING) || cause.equals(DamageCause.FALL) || cause.equals(DamageCause.SUICIDE)) {
					for(Arena a : plugin.getArenaSet()) {
						if(a instanceof PVPArena) {
							if(a.hasThisPlayer(dead)) {
								((PVPArena) a).broadcastSuicide(dead);
							a.decrementTeamPoints(a.getCombatant(dead).getTeam());
							}
						}
					}
				}
			}
		}
	}
}

package org.SkyCraft.Coliseum.Listeners;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

	ColiseumPlugin plugin;

	public DamageListener(ColiseumPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damager = (Player) e.getDamager();
			Player damagee = (Player) e.getEntity();
			for(Arena a : plugin.getArenaSet()) {
				if(a.hasThisPlayer(damagee)) {
					if(a.hasThisPlayer(damager)) {
						if(a.isStarted()) {
							if(a.getCombatant(damagee).getTeam().equalsIgnoreCase(a.getCombatant(damager).getTeam())
								&& !a.getCombatant(damagee).isOldCombatant() && !a.getCombatant(damager).isOldCombatant()) {
								e.setCancelled(true);
								return;
							}
						}
						else {
							e.setCancelled(true);
							return;
						}
					}
					else if(!a.getCombatant(damagee).isOldCombatant()) {
						e.setCancelled(true);
						return;
					}
				}
				else if(a.hasThisPlayer(damager)) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}

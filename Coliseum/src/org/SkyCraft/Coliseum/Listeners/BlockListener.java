package org.SkyCraft.Coliseum.Listeners;

import org.SkyCraft.Coliseum.ColiseumPlugin;
import org.SkyCraft.Coliseum.Arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

	ColiseumPlugin plugin;

	public BlockListener(ColiseumPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleBlockBreak(BlockBreakEvent e) {
		Player breaker = e.getPlayer();
		Block block = e.getBlock();
		
		for(Arena a: plugin.getArenaSet()) {
			if(a.hasThisPlayer(breaker) || 
					(a.getRegion().isBlockContained(block.getLocation()) && a.isEnabled()/* && player has a permission*/) || 
					(a.getWaitingRegion().isBlockContained(block.getLocation()) && a.isEnabled())/* && player has a permission*/) {
				e.setCancelled(true);
				breaker.sendMessage(ChatColor.GRAY + "[Coliseum] This block was not allowed to be broken.");
			}
		}
	}
	
	@EventHandler
	public void handleBlockPlace(BlockPlaceEvent e) {
		Player breaker = e.getPlayer();
		Block block = e.getBlock();
		
		for(Arena a: plugin.getArenaSet()) {
			if(a.hasThisPlayer(breaker) || 
					(a.getRegion().isBlockContained(block.getLocation()) && a.isEnabled()/* && player has a permission*/) || 
					(a.getWaitingRegion().isBlockContained(block.getLocation()) && a.isEnabled())/* && player has a permission*/) {
				e.setCancelled(true);
				breaker.sendMessage(ChatColor.GRAY + "[Coliseum] This block was not allowed to be placed.");
			}
		}
	}
}

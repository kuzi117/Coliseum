package org.SkyCraft.Coliseum;

import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class ColiseumCommandExecutor implements CommandExecutor {

	private ColiseumPlugin plugin;
	private Logger log;

	ColiseumCommandExecutor(ColiseumPlugin plugin, Logger log) {
		this.plugin = plugin;
		this.log = log;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String aliasUsed, String[] args) {
		if(sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
			sender.sendMessage("[Colisuem] Coliseum can only be used in-game.");
			return true;
		}

		else {
			if(args.length == 0) {
				sender.sendMessage("[Colisuem] You need to add arguments to do anything useful.");
			}
			
			String argument = args[0];
			if(argument.equalsIgnoreCase("edit") && args.length >= 2) {
				for(Arena a : plugin.getArenaSet()) {
					if(a.isThisArena(args[1])) {
						a.setPlayerEditing((Player) sender);
						return true;
					}
				}
				sender.sendMessage(ChatColor.GRAY + "No arena was found by that name.");
				return true;
			}
			else if(argument.equalsIgnoreCase("posone")) {
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos1(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("postwo")) {
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos2(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("spawn") && args.length >= 2) {
				//TODO (got sidetracked implementing editor.)
			}
			else if(argument.equalsIgnoreCase("join") && !plugin.isPlayerJoined(((Player) sender).getName()) && args.length >= 2) {
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						sender.sendMessage(ChatColor.GRAY + "You're still editing something. Quit editing and try to join again.");
						return true;
					}
				}
				for(Arena a : plugin.getArenaSet()) {
					if(a.isThisArena(args[1])) {
						a.addPlayer(((Player) sender));
						plugin.joinPlayer(((Player) sender).getName());
						return true;
					}
					((Player) sender).sendMessage(ChatColor.GRAY + "This arena doesn't exist.");
					return true;
				}
			}
			else if(argument.equalsIgnoreCase("leave") && plugin.isPlayerJoined(((Player) sender).getName()) && args.length >= 1) {
				for(Arena a : plugin.getArenaSet()) {
					if(a.hasThisPlayer(((Player) sender))) {
						a.removePlayer(((Player) sender));
						plugin.leavePlayer(((Player) sender).getName());
						return true;
					}
					((Player) sender).sendMessage(ChatColor.GRAY + "You're not in an arena.");
					return true;
				}
			}
			//TODO implement other commands (postwo etc, enable, etc)
			return true;
		}

	}
}

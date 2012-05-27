package org.SkyCraft.Coliseum;

import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Arena.PVPArena;
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
			
			if(argument.equalsIgnoreCase("create") && args.length >= 3) {//TODO editor permissions
				if(args[1].equalsIgnoreCase("pvp")) {
					StringBuilder sb = new StringBuilder();
					for(int i = 2; i <= (args.length - 1); i++) {
						if(i == args.length) {
							sb.append(args[i]);
							break;
						}
						sb.append(args[i] + " ");
					}
					plugin.getArenaSet().add(new PVPArena(sb.toString()));
					sender.sendMessage("[Colisuem] Created a new arena called " + sb.toString() + ".");
					return true;
				}
				else {
					sender.sendMessage("[Colisuem] Not a recognized arena type.");
					return true;
				}
			}
			else if(argument.equalsIgnoreCase("edit")) {//TODO editor permissions
				if(args.length >=2) {
					for(Arena a : plugin.getArenaSet()) {
						if(a.isThisArena(args[1])) {
							for(Arena a2 : plugin.getArenaSet()) {
								if(a2.isPlayerEditing((Player) sender)) {
									a2.removeEditor((Player) sender);
									sender.sendMessage(ChatColor.GRAY + "You are no longer editing " + a.getName() + ".");
									break;
								}
							}
							a.setPlayerEditing((Player) sender);
							sender.sendMessage("[Colisuem] Now editing " + a.getName() + ".");
							return true;
						}
					}
					sender.sendMessage(ChatColor.GRAY + "[Colisuem] No arena was found by that name.");
					return true;
				}
				else {
					for(Arena a : plugin.getArenaSet()) {
						if(a.isPlayerEditing((Player) sender)) {
							a.removeEditor((Player) sender);
							sender.sendMessage(ChatColor.GRAY + "[Coliseum] You are no longer editing " + a.getName() + ".");
							return true;
						}
					}
					sender.sendMessage(ChatColor.GRAY + "[Coliseum] You need to supply an arena to begin editing or you were not editing anything to be removed from.");
					
				}
			}
			else if(argument.equalsIgnoreCase("posone")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos1(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("postwo")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos2(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("spawn") && args.length >= 2) {//TODO editor permissions
				//TODO (got sidetracked implementing editor.)
			}
			else if(argument.equalsIgnoreCase("join") && !plugin.isPlayerJoined(((Player) sender).getName()) && args.length >= 2) {//TODO player permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						sender.sendMessage(ChatColor.GRAY + "[Colisuem] You're still editing something. Quit editing and try to join again.");
						return true;
					}
				}
				for(Arena a : plugin.getArenaSet()) {
					if(a.isThisArena(args[1])) {
						a.addPlayer(((Player) sender));
						plugin.joinPlayer(((Player) sender).getName());
						sender.sendMessage(ChatColor.GRAY + "[Coliseum] Welcome to " + a.getName() + "!");
						return true;
					}
					((Player) sender).sendMessage(ChatColor.GRAY + "[Colisuem] No arena was found by that name.");
					return true;
				}
			}
			else if(argument.equalsIgnoreCase("leave") && plugin.isPlayerJoined(((Player) sender).getName())) {//TODO player permissions
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
			else if(argument.equalsIgnoreCase("enable") && args.length >= 2) {//TODO Admin (and/or editor) permissions
				for(Arena a: plugin.getArenaSet()) {
					if(a.isThisArena(args[1])) {
						if(a.enable()) {
							sender.sendMessage(ChatColor.GRAY + "[Coliseum] Arena was successfully enabled!");
							return true;
						}
						sender.sendMessage(ChatColor.GRAY + "[Coliseum] This arena was not ready to be enabled for some reason.");
						return true;
					}
				}
				((Player) sender).sendMessage(ChatColor.GRAY + "[Colisuem] No arena was found by that name.");
			}
			//TODO implement other commands (kick, enable, disable, )
			return true;
		}

	}
}

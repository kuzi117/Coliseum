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
				return true;
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
					PVPArena a = new PVPArena(sb.toString());
					plugin.getArenaSet().add(a);
					sender.sendMessage("[Colisuem] Created a new PVP arena called " + sb.toString() + ".");
					for(Arena a2 : plugin.getArenaSet()) {
						if(a2.isPlayerEditing((Player) sender)) {
							a2.removeEditor((Player) sender);
							sender.sendMessage(ChatColor.GRAY + "You are no longer editing " + a.getName() + ".");
							break;
						}
					}
					a.setPlayerEditing((Player) sender);
					sender.sendMessage("[Colisuem] Now editing " + sb.toString() + ".");
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
					return true;
				}
			}
			else if(argument.equalsIgnoreCase("posone") || argument.equalsIgnoreCase("po") || argument.equalsIgnoreCase("p1")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos1(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("wposone") || argument.equalsIgnoreCase("wpo") || argument.equalsIgnoreCase("wp1")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getWaitingRegion().setPos1(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("postwo") || argument.equalsIgnoreCase("pt") || argument.equalsIgnoreCase("p2")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getRegion().setPos2(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("wpostwo") || argument.equalsIgnoreCase("wpt") || argument.equalsIgnoreCase("wp2")) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						a.getWaitingRegion().setPos2(((Player) sender).getTargetBlock(null, 10));
						return true;
					}//TODO Check if block is null (looking too far away), do later in interest of getting this done.
				}
			}
			else if(argument.equalsIgnoreCase("spawn") && args.length >= 2) {//TODO editor permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						if(a instanceof PVPArena) {
							StringBuilder sb = new StringBuilder();
							for(int i = 1; i <= (args.length - 1); i++) {
								if(i == args.length) {
									sb.append(args[i]);
									break;
								}
								sb.append(args[i] + " ");
							}
							if(sb.toString().equalsIgnoreCase("waiting") || sb.toString().equalsIgnoreCase("wait") || sb.toString().equalsIgnoreCase("w")) {
								((PVPArena) a).getWaitingRegion().setSpawn(((Player) sender).getTargetBlock(null, 10).getLocation());
								sender.sendMessage(ChatColor.GRAY + "[Coliseum] Waiting area spawn set!");
								return true;
							}
							else if(((PVPArena) a).getTeams().containsKey(sb.toString())) {//TODO if containsKey is case sensitive need new way to match arena names
								((PVPArena) a).getRegion().addTeamSpawn(args[1], ((Player) sender).getTargetBlock(null, 10).getLocation());
								sender.sendMessage(ChatColor.GRAY + "[Coliseum] Team " + sb.toString() + " spawn was created!");
								return true;
							}
							else {
								sender.sendMessage(ChatColor.GRAY + "[Coliseum] No spawn with that name was found. No spawn was set.");
							}
						}
					}
				}
			}
			else if(argument.equalsIgnoreCase("join") && !plugin.isPlayerJoined(((Player) sender).getName()) && args.length >= 2) {//TODO player permissions
				for(Arena a : plugin.getArenaSet()) {
					if(a.isPlayerEditing((Player) sender)) {
						sender.sendMessage(ChatColor.GRAY + "[Colisuem] You're still editing something. Quit editing and try to join again.");
						return true;
					}
				}
				
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i <= (args.length - 1); i++) {
					if(i == args.length) {
						sb.append(args[i]);
						break;
					}
					sb.append(args[i] + " ");
				}
				
				for(Arena a : plugin.getArenaSet()) {
					if(a.isThisArena(sb.toString())) {
						a.addPlayer(((Player) sender));
						plugin.joinPlayer(((Player) sender).getName());
						sender.sendMessage(ChatColor.GRAY + "[Coliseum] Welcome to " + a.getName() + "!");
						return true;
					}
					sender.sendMessage(ChatColor.GRAY + "[Colisuem] No arena was found by that name.");
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
					sender.sendMessage(ChatColor.GRAY + "[Coliseum] You're not in an arena.");
					return true;
				}
			}
			else if(argument.equalsIgnoreCase("enable") && args.length >= 2) {//TODO Admin (and/or editor) [multiple permissions? admin can boot, editor can't?]
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i <= (args.length - 1); i++) {
					if(i == args.length) {
						sb.append(args[i]);
						break;
					}
					sb.append(args[i] + " ");
				}
				
				for(Arena a: plugin.getArenaSet()) {
					if(a.isThisArena(sb.toString())) {
						if(a.enable()) {
							sender.sendMessage(ChatColor.GRAY + "[Coliseum] Arena was successfully enabled!");
							return true;
						}
						sender.sendMessage(ChatColor.GRAY + "[Coliseum] This arena was not ready to be enabled for some reason.");
						return true;
					}
				}
				sender.sendMessage(ChatColor.GRAY + "[Colisuem] No arena was found by that name.");
				return true;
			}
			//TODO implement other commands (kick, forcestart, createteam)
			//TODO implement a "help" message.
			return true;
		}

	}
}

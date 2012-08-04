package org.SkyCraft.Coliseum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Arena.PVPArena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
	private ColiseumPlugin plugin;
	private Logger log;
	private FileConfiguration config;
	private YamlConfiguration arenas;
	private boolean sendStartupMessages;

	public ConfigHandler(ColiseumPlugin plugin, Logger log) {
		this.plugin = plugin;
		this.log = log;

		File file = new File(plugin.getDataFolder(), "config.yml");
		if (!file.exists()) {
			plugin.saveDefaultConfig();
		}
		this.config = plugin.getConfig();
		
		loadPluginConfig();
		loadArenasConfig();
	}

	private void loadPluginConfig() {
		plugin.getPlayerListener().setHandleIgnoredDamage(config.getBoolean("handle-ignored-damage"));
		sendStartupMessages = config.getBoolean("send-startup-messages");
		return;
	}

	private void loadArenasConfig() {
		try {
			File file = new File(plugin.getDataFolder(), "arenas.yml");
			arenas = new YamlConfiguration();
			arenas.load(file);
		} catch (FileNotFoundException e) {
			File file = new File(plugin.getDataFolder(), "arenas.yml");
			try {
				new YamlConfiguration().save(file);
				loadArenasConfig();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			log.info("Problem reading arena configs from disc.");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			log.info("Arena config was set up incorrectly.");
			e.printStackTrace();
		}
	}

	public void createArena(String arenaName, String arenaType) {
		arenas.createSection(arenaName);
		arenas.set(arenaName + ".enabled", false);
		arenas.set(arenaName + ".type", arenaType);
		saveArenas();
		return;
	}

	public void setArenaEnabledState(String arenaName, boolean state) {
		arenas.set(arenaName + ".enabled", state);
		saveArenas();
		return;
	}

	public void setArenaPos(int posNum, String arenaName, Location loc) {
		if(posNum <= 2) {
			arenas.set(arenaName + ".pos" + posNum + ".x", loc.getBlockX());
			arenas.set(arenaName + ".pos" + posNum + ".y", loc.getBlockY());
			arenas.set(arenaName + ".pos" + posNum + ".z", loc.getBlockZ());
			arenas.set(arenaName + ".pos" + posNum + ".world", loc.getWorld());
		}
		else if(posNum > 2) {
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".x", loc.getBlockX());
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".y", loc.getBlockY());
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".z", loc.getBlockZ());
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".world", loc.getWorld());
		}
		saveArenas();
		return;
	}

	public void setSpawn(String arenaName, String team, Location loc) {
		arenas.set(arenaName + ".spawns." + team + ".x", loc.getX());
		arenas.set(arenaName + ".spawns." + team + ".y", loc.getY());
		arenas.set(arenaName + ".spawns." + team + ".z", loc.getZ());
		arenas.set(arenaName + ".spawns." + team + ".yaw", loc.getYaw());
		arenas.set(arenaName + ".spawns." + team + ".pitch", loc.getPitch());
		arenas.set(arenaName + ".spawns." + team + ".world", loc.getWorld().getName());
		saveArenas();
		return;
	}

	public void removeTeam(String arenaName, String teamName) {
		arenas.set(arenaName + ".spawns." + teamName, null);
		return;
	}

	private void saveArenas() {
		try {
			arenas.save(new File(plugin.getDataFolder(), "arenas.yml"));
		} catch (IOException e) {
			log.info("Arenas.yml was unable to be saved for an unknown reason.");
			e.printStackTrace();
		}
	}

	public void loadArenas() {
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		Set<String> arenaNames = arenas.getValues(false).keySet();
		int loaded = 0;
		int enabled = 0;

		for(String name : arenaNames) {
			Arena a = null;
			ConfigurationSection sec = arenas.getConfigurationSection(name);
			if(sec.contains("type")) {
				String type = sec.getString("type");
				if(type.equalsIgnoreCase("pvp")) {
					a = new PVPArena(name, plugin);
					plugin.getArenaSet().add(a);
				}
				else {
					if(sendStartupMessages) {
						console.sendMessage("[Coliseum] Arena " + ChatColor.RED + name + ChatColor.GRAY + " was not loaded, had unsupported arena type " + ChatColor.RED + type + ChatColor.GRAY + ".");
						console.sendMessage("[Coliseum] Arena " + "Supported arena types are: " + ChatColor.GOLD + "pvp.");
					}
					continue;
				}
			}
			else {
				if(sendStartupMessages) {
					console.sendMessage("[Coliseum] Arena " + ChatColor.RED + name + ChatColor.GRAY + " was unsuccessfully loaded, was missing arena type.");
				}
				continue;
			}
			if(sec.contains("pos1") && sec.contains("pos1.x") && sec.contains("pos1.y") && sec.contains("pos1.z")) {
				a.getRegion().setPos1(sec.getInt("pos1.x"), sec.getInt("pos1.y"), sec.getInt("pos1.z"), plugin.getServer().getWorld(sec.getString("pos1.world")));
			}
			if(sec.contains("pos2") && sec.contains("pos2.x") && sec.contains("pos2.y") && sec.contains("pos2.z")) {
				a.getRegion().setPos2(sec.getInt("pos2.x"), sec.getInt("pos2.y"), sec.getInt("pos2.z"), plugin.getServer().getWorld(sec.getString("pos2.world")));
			}
			if(sec.contains("wpos1") && sec.contains("wpos1.x") && sec.contains("wpos1.y") && sec.contains("wpos1.z")) {
				a.getWaitingRegion().setPos1(sec.getInt("wpos1.x"), sec.getInt("wpos1.y"), sec.getInt("wpos1.z"), plugin.getServer().getWorld(sec.getString("wpos1.world")));
			}
			if(sec.contains("wpos2") && sec.contains("wpos2.x") && sec.contains("wpos2.y") && sec.contains("wpos2.z")) {
				a.getWaitingRegion().setPos2(sec.getInt("wpos2.x"), sec.getInt("wpos2.y"), sec.getInt("wpos2.z"), plugin.getServer().getWorld(sec.getString("wpos2.world")));
			}
			if(sec.contains("spawns")) {
				ConfigurationSection sec2 =  sec.getConfigurationSection("spawns");
				Set<String> teamNames = sec2.getValues(false).keySet();
				for(String teamName : teamNames) {
					if(teamName.equalsIgnoreCase("waitingareaspawn")) {
						if(sec2.contains(teamName + ".x") && sec2.contains(teamName + ".y") && sec2.contains(teamName + ".z") && sec2.contains(teamName + ".yaw")  && sec2.contains(teamName + ".pitch") && sec2.contains(teamName + ".world")) {
							a.getWaitingRegion().setSpawn(new Location(plugin.getServer().getWorld(sec2.getString(teamName + ".world")), sec2.getDouble(teamName + ".x"), 
									sec2.getDouble(teamName + ".y"), sec2.getDouble(teamName + ".z"), (float) sec2.getDouble(teamName + ".yaw"), (float) sec2.getDouble(teamName + ".pitch")));
						}
					}
					else {
						a.addTeamName(teamName);
						if(sec2.contains(teamName + ".x") && sec2.contains(teamName + ".y") && sec2.contains(teamName + ".z") && sec2.contains(teamName + ".yaw") && sec2.contains(teamName + ".pitch") && sec2.contains(teamName + ".world")) {
							a.getRegion().addTeamSpawn(teamName, new Location(plugin.getServer().getWorld(sec2.getString(teamName + ".world")), sec2.getDouble(teamName + ".x"),
									sec2.getDouble(teamName + ".y"), sec2.getDouble(teamName + ".z"), (float) sec2.getDouble(teamName + ".yaw"), (float) sec2.getDouble(teamName + ".pitch")));
						}
					}
				}
			}
			if(sec.contains("enabled") && sec.getBoolean("enabled")) {
				if(!a.enable()) {
					sec.set("enabled", false);
					if(sendStartupMessages) {
						console.sendMessage("[Coliseum] Loaded " + ChatColor.RED + a.getName() + ChatColor.GRAY + " successfully but was disabled.");
					}
					loaded++;
				}
				else {
					if(sendStartupMessages) {
						console.sendMessage("[Coliseum] Loaded " + ChatColor.GREEN + a.getName() + ChatColor.GRAY + " successfully and was enabled.");
					}
					loaded++;
					enabled++;
				}
			}
			else {
				sec.set("enabled", false);
				if(sendStartupMessages) {
					console.sendMessage("[Coliseum] Loaded " + ChatColor.RED + a.getName() + ChatColor.GRAY + " successfully but was disabled.");
				}
				loaded++;
			}
		}
		if(sendStartupMessages) {
			console.sendMessage("[Coliseum] " + ChatColor.GREEN + String.valueOf(loaded) + "/" + arenaNames.size() + ChatColor.GRAY + " arenas were loaded.");
			console.sendMessage("[Coliseum] " + ChatColor.GREEN + String.valueOf(enabled) + "/" + arenaNames.size() + ChatColor.GRAY + " arenas were enabled.");
		}
	}

}

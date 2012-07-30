package org.SkyCraft.Coliseum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import org.SkyCraft.Coliseum.Arena.Arena;
import org.SkyCraft.Coliseum.Arena.PVPArena;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class ConfigHandler {
	private ColiseumPlugin plugin;
	private Logger log;
	private FileConfiguration config;
	private YamlConfiguration arenas;

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

	public void setArenaPos(int posNum, String arenaName, Vector vec) {
		if(posNum <=2) {
			arenas.set(arenaName + ".pos" + posNum + ".x", vec.getBlockX());
			arenas.set(arenaName + ".pos" + posNum + ".y", vec.getBlockY());
			arenas.set(arenaName + ".pos" + posNum + ".z", vec.getBlockZ());
		}
		else if(posNum > 2) {
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".x", vec.getBlockX());
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".y", vec.getBlockY());
			arenas.set(arenaName + ".wpos" + (posNum - 2) + ".z", vec.getBlockZ());
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
		Set<String> arenaNames = arenas.getValues(false).keySet();

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
					continue;
				}
			}
			else {
				continue;
			}
			if(sec.contains("pos1")) {//TODO Add check if contains x, y, z
				if (sec.contains("pos1.x") && sec.contains("pos1.y") && sec.contains("pos1.z")) {
					a.getRegion().setPos1(sec.getInt("pos1.x"), sec.getInt("pos1.y"), sec.getInt("pos1.z"));
				}
			}
			if(sec.contains("pos2")) {
				if (sec.contains("pos2.x") && sec.contains("pos2.y") && sec.contains("pos2.z")) {
					a.getRegion().setPos2(sec.getInt("pos2.x"), sec.getInt("pos2.y"), sec.getInt("pos2.z"));
				}
			}
			if(sec.contains("wpos1")) {
				if (sec.contains("wpos1.x") && sec.contains("wpos1.y") && sec.contains("wpos1.z")) {
					a.getWaitingRegion().setPos1(sec.getInt("wpos1.x"), sec.getInt("wpos1.y"), sec.getInt("wpos1.z"));
				}
			}
			if(sec.contains("wpos2")) {
				if (sec.contains("wpos2.x") && sec.contains("wpos2.y") && sec.contains("wpos2.z")) {
					a.getWaitingRegion().setPos2(sec.getInt("wpos2.x"), sec.getInt("wpos2.y"), sec.getInt("wpos2.z"));
				}
			}
			if(sec.contains("spawns")) {
				ConfigurationSection sec2 =  sec.getConfigurationSection("spawns");
				Set<String> teamNames = sec2.getValues(false).keySet();
				for(String teamName : teamNames) {
					if(teamName.equalsIgnoreCase("waitingareaspawn")) {
						if(sec2.contains(teamName + ".x") && sec2.contains(teamName + ".y") && sec2.contains(teamName + ".z") && sec2.contains(teamName + ".y")) {
							a.getWaitingRegion().setSpawn(new Location(plugin.getServer().getWorld(sec2.getString(teamName + ".world")), sec2.getDouble(teamName + ".x"), 
									sec2.getDouble(teamName + ".y"), sec2.getDouble(teamName + ".z"), (float) sec2.getDouble(teamName + ".yaw"), (float) sec2.getDouble(teamName + ".pitch")));
						}
					}
					else {
						a.addTeamName(teamName);
						if(sec2.contains(teamName + ".x") && sec2.contains(teamName + ".y") && sec2.contains(teamName + ".z") && sec2.contains(teamName + ".y")) {
							a.getRegion().addTeamSpawn(teamName, new Location(plugin.getServer().getWorld(sec2.getString(teamName + ".world")), sec2.getDouble(teamName + ".x"),
									sec2.getDouble(teamName + ".y"), sec2.getDouble(teamName + ".z"), (float) sec2.getDouble(teamName + ".yaw"), (float) sec2.getDouble(teamName + ".pitch")));
						}
					}
				}
			}
			if(sec.contains("enabled")) {
				if(sec.getBoolean("enabled")) {
					if(!a.enable()) {
						sec.set("enabled", false);
					}
				}
			}
			else {
				sec.set("enabled", false);
			}
		}

	}

}

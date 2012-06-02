package org.SkyCraft.Coliseum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Location;
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
		
		loadArenasConfig();
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
	
	public void setSpawn(String arenaName, String spawn, Location loc) {
		arenas.set(arenaName + ".spawns." + spawn + ".x", loc.getX());
		arenas.set(arenaName + ".spawns." + spawn + ".y", loc.getY());
		arenas.set(arenaName + ".spawns." + spawn + ".z", loc.getZ());
		saveArenas();
		return;
	}
	
	public void addTeam(String arenaName, String teamName) {
		arenas.set(arenaName + ".teams", teamName);
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

}

package org.SkyCraft.Coliseum;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	private ColiseumPlugin plugin;
	private FileConfiguration config;
	
	public ConfigHandler(ColiseumPlugin plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
		 
		if (new File(plugin.getDataFolder(), "config.yml").exists()) {
		    plugin.saveDefaultConfig();
		    this.config = plugin.getConfig();
		}
	}
	
	void loadSomething()  {
		
	}

}

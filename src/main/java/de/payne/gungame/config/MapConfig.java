package de.payne.gungame.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

import de.payne.gungame.map.GameMap;


public class MapConfig {
	
	private final File mapFile;
	private final YamlConfiguration mapConfiguration;
	
	//Konstruktor
	public MapConfig(final Plugin plugin, final String fileName) {
		this.mapFile = new File(plugin.getDataFolder() + File.separator + fileName);
//		this.mapConfiguration = YamlConfiguration.loadConfiguration(this.mapFile);
		
		if(!this.mapFile.exists()) {
			try {
				this.mapFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.mapConfiguration = YamlConfiguration.loadConfiguration(this.mapFile);

	}
	
	//saves the File
	public final void saveFile() {
		try {
			this.mapConfiguration.save(this.mapFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//returns true if maps inside the config, else false
	public final boolean mapsInsideFile() {
		return this.mapConfiguration.contains("maps");
	}
	
	//returns true if the map already exists
	public final boolean alreadyExists(final String mapName) {
		return this.mapConfiguration.contains("maps." + mapName);
	}
	
	//registers a new Map in file
	public final void registerMap(final String mapName, final Location spawnLocation, final String builder, final Boolean votable, final Boolean teamsAllowed, final String protectionform, final int radius, final int xAchse, final int zAchse) {
		final String path = "maps." + mapName + ".";
		
		this.mapConfiguration.set(path + "spawnLocation.world", spawnLocation.getWorld().getName());
		this.mapConfiguration.set(path + "spawnLocation.x", spawnLocation.getX());
		this.mapConfiguration.set(path + "spawnLocation.y", spawnLocation.getY());
		this.mapConfiguration.set(path + "spawnLocation.z", spawnLocation.getZ());
		this.mapConfiguration.set(path + "spawnLocation.yaw", spawnLocation.getYaw());
		this.mapConfiguration.set(path + "spawnLocation.pitch", spawnLocation.getPitch());
		
		this.mapConfiguration.set(path + "builder", builder);
		this.mapConfiguration.set(path + "votable", votable);
		this.mapConfiguration.set(path + "teamsAllowed", teamsAllowed);
		
		this.mapConfiguration.set(path + "protection.type", protectionform);
		this.mapConfiguration.set(path + "protection.radius", radius);
		this.mapConfiguration.set(path + "protection.xAchse", xAchse);
		this.mapConfiguration.set(path + "protection.zAchse", zAchse);

		this.saveFile();
	}
	
	public final void deleteMap(final String mapName) {
		if(!alreadyExists(mapName)) {
			return;
		}
		this.mapConfiguration.set("maps." + mapName, null);
		this.saveFile();
	}
	
	public final void setVotable(final String mapName, final boolean votable) {
		if(!alreadyExists(mapName)) {
			return;
		}
		this.mapConfiguration.set("maps." + mapName + ".votable", votable);
		this.saveFile();
	}
	
	//get a Map from file returns a gamemap objekt
	public final GameMap getGameMap(final String mapName) {
		final String path = "maps." + mapName + ".";
	
		final String spawnWorld = this.mapConfiguration.getString(path + "spawnLocation.world"); 
		final double x = this.mapConfiguration.getDouble(path + "spawnLocation.x");
		final double y = this.mapConfiguration.getDouble(path + "spawnLocation.y");
		final double z = this.mapConfiguration.getDouble(path + "spawnLocation.z");
		final float yaw = (float) this.mapConfiguration.getDouble(path + "spawnLocation.yaw");
		final float pitch = (float) this.mapConfiguration.getDouble(path + "spawnLocation.pitch");
		
		final String builder = this.mapConfiguration.getString(path + "builder");
		final Boolean votable = this.mapConfiguration.getBoolean(path + "votable");
		final Boolean teamsAllowed = this.mapConfiguration.getBoolean(path + "teamsAllowed");
		
		final String protectionform = this.mapConfiguration.getString(path + "protection.type");
		final int radius = this.mapConfiguration.getInt(path + "protection.radius");
		final int xAchse = this.mapConfiguration.getInt(path + "protection.xAchse");
		final int zAchse = this.mapConfiguration.getInt(path + "protection.zAchse");

		
		final Location spawnLocation = new Location(Bukkit.getWorld(spawnWorld), x, y, z, yaw, pitch);
		final GameMap gameMap = new GameMap(mapName, spawnLocation, builder, votable, teamsAllowed, protectionform, radius, xAchse, zAchse );
		
		return gameMap;
	}
	
	//get all GameMap objekts saved in file
	public final List<GameMap> getGameMaps() {
		final List<GameMap> maps = Lists.newArrayList();
		
		for(final String nextMap : this.mapConfiguration.getConfigurationSection("maps").getKeys(false)) {
			maps.add(this.getGameMap(nextMap));
		}
		
		return maps;
	}
	
	//only gets the votable GameMap objekts saved in file
	public final List<GameMap> getVotableMaps() {
		final List<GameMap> maps = Lists.newArrayList();
		
		for(final String nextMap : this.mapConfiguration.getConfigurationSection("maps").getKeys(false)) {
			final String path = "maps." + nextMap + ".votable";

			if(this.mapConfiguration.getBoolean(path)) {
				maps.add(this.getGameMap(nextMap));
			}
		}
		return maps;

	}

}

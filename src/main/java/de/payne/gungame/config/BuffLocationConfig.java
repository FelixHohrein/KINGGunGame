package de.payne.gungame.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.buffs.BuffLocations;
import de.payne.gungame.map.GameMap;

public class BuffLocationConfig {
	
	private final File buffFile;
	private final YamlConfiguration buffConfiguration;
	
	//Konstruktor
	public BuffLocationConfig(final Plugin plugin, final String fileName) {
		this.buffFile = new File(plugin.getDataFolder() + File.separator + fileName);
//		this.buffConfiguration = YamlConfiguration.loadConfiguration(this.buffFile);
		
		if(!this.buffFile.exists()) {
			try {
				this.buffFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.buffConfiguration = YamlConfiguration.loadConfiguration(this.buffFile);

	}
	
	//saves the File
	public final void saveFile() {
		try {
			this.buffConfiguration.save(this.buffFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//returns true if the path exists
	public final boolean isBuffExists() {
		return this.buffConfiguration.contains("buffs");
	}
	
	//returns true if the path exists
	public final boolean isMapBuffExists(GameMap gameMap) {
		return this.buffConfiguration.contains("buffs." + gameMap.getMapname());
	}
	
	//setTopPlayer Location
	public final void addBuffPosition(final GameMap gameMap, final Location location, final int locationID) {
		this.buffConfiguration.set("buffs." + gameMap.getMapname() + "." + locationID, location);
		this.saveFile();
	}
	
	//DELETE A BUFFLOCATION
	public final void deleteBuffLocation(final String mapName, final int locationID) {
		if(!alreadyExists(mapName, locationID)) {
			return;
		}
		this.buffConfiguration.set("buffs." + mapName + "." + locationID, null);
		this.saveFile();
	}
	
	//returns true if the BUFFLOCATION already exists
	public final boolean alreadyExists(final String mapName, final int locationID) {
		return this.buffConfiguration.contains("buffs." + mapName + "." + locationID);
	}
	
	//Gets the BufflOcations Object
	public final BuffLocations getBuffLocation(final String mapName, final int id) {
		
		GameMap gameMap = GunGame.getInstance().getMapConfig().getGameMap(mapName);
		int locationID = id;
		Location buffLocation = this.buffConfiguration.getLocation("buffs." + mapName + "." + id);

		
		return new BuffLocations(gameMap, locationID, buffLocation);
	}

	//gets returns a List of all BuffPositions Objectrs of the Map
	public final List <BuffLocations> getBuffPositions(final String mapName) {
		
		final List <BuffLocations> zwischenspeicher = Lists.newArrayList();
		
		if(!this.isBuffExists()) {
			return null;
		}

		for(final String next : this.buffConfiguration.getConfigurationSection("buffs." + mapName).getKeys(false)) {
			zwischenspeicher.add(this.getBuffLocation(mapName, Integer.parseInt(next)));
		}

		
		return zwischenspeicher;
	}
	
}

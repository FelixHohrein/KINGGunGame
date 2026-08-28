package de.payne.gungame.buffs;

import org.bukkit.Location;

import de.payne.gungame.map.GameMap;
import lombok.Getter;

public final class BuffLocations {

	@Getter
	private GameMap gameMap;
	@Getter
	private int locationID;
	@Getter
	private Location location;
	
	public BuffLocations(final GameMap gameMap, final int locationID, final Location location) {
		this.gameMap = gameMap;
		this.locationID = locationID;
		this.location = location;
	}
}

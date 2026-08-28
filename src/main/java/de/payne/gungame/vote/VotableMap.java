package de.payne.gungame.vote;

import org.bukkit.Location;

import de.payne.gungame.map.GameMap;
import lombok.Getter;
import lombok.Setter;


public class VotableMap extends GameMap {
	
	@Getter
	@Setter
	private int votes = 0;
	
	//Konstruktor
	public VotableMap(final String name, final Location spawLocation, final String builder, final Boolean votable, final Boolean teamsAllowed, final String protectiontype, final int radius, final int xAchse, final int zAchse) {
		super(name, spawLocation, builder, votable, teamsAllowed, protectiontype, radius, xAchse, zAchse);
	}
	//2. Konstruktor fertiges gameMap objekt
	public VotableMap(final GameMap gameMap) {
		this(gameMap.getMapname(), gameMap.getSpawnLocation(), gameMap.getBuilder(), gameMap.getVotable(), gameMap.getTeamsErlaubt(), gameMap.getProtectionform(), gameMap.getRadius(), gameMap.getXAchse(), gameMap.getZAchse());
	}	
}

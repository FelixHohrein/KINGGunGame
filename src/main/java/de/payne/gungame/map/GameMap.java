package de.payne.gungame.map;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

public class GameMap {
	
	@Getter
	private Location spawnLocation;
	@Getter
	private String builder, mapname, protectionform;
	@Getter
	@Setter
	private Boolean votable, teamsErlaubt;
	@Getter
	private int radius, xAchse, zAchse;
	
	//Konstruktor
	public GameMap(final String mapname, final Location spawLocation, final String builder, final Boolean votable, final Boolean teamsAllowed, final String protectionform, final int radius, final int xAchse, final int zAchse) {
		this.mapname = mapname;
		this.spawnLocation = spawLocation;
		this.builder = builder;
		this.votable = votable;
		this.teamsErlaubt = teamsAllowed;
		this.protectionform = protectionform;
		this.radius = radius;
		this.xAchse = xAchse;
		this.zAchse = zAchse;
	}
}

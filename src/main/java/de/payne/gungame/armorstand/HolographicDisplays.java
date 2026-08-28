package de.payne.gungame.armorstand;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.database.GunGamePlayer;
import de.payne.gungame.database.StatistikTable;
import net.md_5.bungee.api.ChatColor;

public class HolographicDisplays {

	private StatistikTable statistikTable;
	
	
	private Location location;
	
	//platz 4-10 holos
	public static  Map<Integer, Hologram> holos;
	
	//platz 4-10 gespeichert
	private final Map<Integer, UUID> uuids;
		
	
	public HolographicDisplays() {
		this.statistikTable = GunGame.getInstance().getStatisticTable();
		this.location = GunGame.getInstance().getSettings().getTopLowerPositions();
		//CONVERT MAPS TO WORK WITH
		this.uuids = Maps.newHashMap();
		this.saveMap();
		
		//CREATE HOLOGRAMS		
		holos = this.setValues();
		
	}
	
//	**********************************************************************
//	FOR POSITION HANDLING
//	**********************************************************************
	private final String getDirection(float yaw) {
		
	    if(yaw >= 315 || yaw <= 45) {
	        // towards north, positive z
	       return "-z";
	    } else if(yaw > 45 && yaw < 135) {
	        // towards west, negative x
	    	 return "-x";
	    } else if(yaw >= 135 && yaw <= 225) {
	        // towards south, negative z
	    	 return "+z";
	    } else if(yaw > 225 && yaw < 315) {
	       // towards east, positive x
	    	 return "+x";
	    }
		return null;
	}
	
	private final Location addLocation (Location loc) {
		String direction = this.getDirection(this.location.getYaw());
		
		switch (direction) {

		case "+z": //-x
			return loc.clone().add(0, 0, 2);
		case "-z": //+x
			return loc.clone().subtract(0, 0, 2);
		case "+x": //+z
			return loc.clone().add(2, 0, 0);
		case "-x": //-z
			return loc.clone().subtract(2, 0, 0);
		}
		return null;
	}
//	*********************************************************************
//	Convert Strings from Database to UUID and put into Map
//	**********************************************************************
	private final void saveMap() {
		Map<Integer, String> topRanks = this.statistikTable.getTopRanks();
		
		for(int i : topRanks.keySet()) {
			
			this.uuids.put(i, UUID.fromString(topRanks.get(i)));
		}
	}
//	**********************************************************************
//  FOR SETTING AND UPDATING THE VALUES
//	**********************************************************************
	public final Map<Integer, Hologram> setValues() {
		
		this.deleteOld();
		this.saveMap();
		
		Map<Integer, Hologram> holos = Maps.newHashMap();
		
		Location location = this.location.clone();
		
		for(int i = 4; i <= 10; i++) {
			
			GunGamePlayer ggPlayer;
			
			if(Bukkit.getOfflinePlayer(this.uuids.get(i)).isOnline()) {
				ggPlayer = GunGame.getInstance().getGungamePlayers().get(this.uuids.get(i));
			} else {
				ggPlayer = new GunGamePlayer(this.uuids.get(i));
			}
			
			Hologram holo = HologramsAPI.createHologram(GunGame.getInstance(), location);

			holo.insertItemLine(0, SkullCreator.itemFromUuid(this.uuids.get(i)));
			holo.insertTextLine(1, ChatColor.GOLD + Bukkit.getOfflinePlayer(this.uuids.get(i)).getName());
			holo.insertTextLine(2, ChatColor.GRAY + "Kills: " + ChatColor.GOLD + ggPlayer.getKills());
			holo.insertTextLine(3, ChatColor.GRAY + "K/D: " + ChatColor.GOLD + ggPlayer.getKd());
			holo.insertTextLine(4, ChatColor.GRAY + "Max Lvl: " + ChatColor.GOLD + ggPlayer.getHighestLevel());
			
			location = this.addLocation(location);
			
			holos.put(i, holo);
		}
		
		return holos;
	}
//	**********************************************************************

	private final void deleteOld() {
		if(holos != null) {
			if(!holos.isEmpty()) {
				for(Hologram holo : holos.values()) {
					holo.delete();
				}
			}
		}
	}
}

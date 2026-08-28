package de.payne.gungame;



import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;


public final class GunGameSettings {
	
	private final FileConfiguration configuration;
	private final Plugin plugin;
	
	public GunGameSettings(final Plugin plugin) {
		this.plugin = plugin;
		this.configuration = plugin.getConfig();
		
		if(!this.plugin.getDataFolder().exists()) {
			this.plugin.getDataFolder().mkdir();
		}
		
		this.plugin.saveDefaultConfig();
	}
//MYSQL
	public final String getHost() {
		return this.configuration.getString("database.host");
	}
	
	public final int getPort() {
		return this.configuration.getInt("database.port");
	}	
	
	public final String getDatabase() {
		return this.configuration.getString("database.database");
	}
	
	public final String getUsername() {
		return this.configuration.getString("database.username");
	}
	
	public final String getPassword() {
		return this.configuration.getString("database.password");
	}
	
	public final boolean isConnectionAllowed() {
		return this.configuration.getBoolean("database.connectionAllowed");
	}
//ALLGEMEIN GUNGAME
	public final String getPrefix() {
//		return Component.text(ChatColor.translateAlternateColorCodes('&', this.configuration.getString("game.prefix")));
		return ChatColor.translateAlternateColorCodes('&', this.configuration.getString("game.prefix"));
	}
	
	public final int getPointsToAdd() {
		return this.configuration.getInt("game.points.add");
	}
	
	public final int getPointsToRemove() {
		return this.configuration.getInt("game.points.remove");
	}
//VOTE MAP SYSTEM
	public final int getVoteTime() {
		return this.configuration.getInt("game.vote.voteTime");
	}
	
	public final boolean isVotePerPlayer() {
		return this.configuration.getBoolean("game.vote.voteDelayPerPlayer");
	}
	
	public final int getVoteDelay() {
		return this.configuration.getInt("game.vote.voteDelay");
	}
	
	public final int getVoteAutoTaskTime() {
		return this.configuration.getInt("game.vote.voteAutoTaskTime");
	}
	
	//return true if the path exists, if a first map signed in with command
	public final boolean signPathExists() {
		return this.configuration.contains("game.lobby.joinSign");
	}
	
	//returns true if the path exists
	public final boolean isShopExists() {
		return this.configuration.contains("game.lobby.shop");
	}
	
	public final boolean topPlayersExists() {
		if(this.configuration.contains("game.lobby.topplayers.location.1") && this.configuration.contains("game.lobby.topplayers.location.2") && this.configuration.contains("game.lobby.topplayers.location.3")) {
			return true;
		} else {
			return false;
		}
	}
	
	public final boolean topPlayersLowerExists() {
		if(this.configuration.contains("game.lobby.topplayers.location.other")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//gets the sign position
	public final Sign getSign() {
		
		if(!this.signPathExists()) {
			return null;
		}
		
		final Location signLocation = this.configuration.getLocation("game.lobby.joinSign.location");
		Block block = signLocation.getWorld().getBlockAt(signLocation);
		Sign sign = (Sign) block.getState();

		return sign;
		
	}
	
	//sets the sign position
	public final void setSignPosition(final Sign sign) {
		
		this.configuration.set("game.lobby.joinSign.location", sign.getLocation());
		this.plugin.saveConfig();
	}
	
	//setTopPlayer Location
	public final void setTopPlayersPosition(final int rang, final Location playerLocation) {
		
		if(rang == 1) {
			this.configuration.set("game.lobby.topplayers.location.1", playerLocation);
		} else if (rang == 2) {
			this.configuration.set("game.lobby.topplayers.location.2", playerLocation);
		} else if (rang == 3) {
			this.configuration.set("game.lobby.topplayers.location.3", playerLocation);
		} else {
			return;
		}
		this.plugin.saveConfig();
	}
	
	//setTopPlayer Location
	public final void setTopLowerPlayersPosition(final Location playerLocation) {

		this.configuration.set("game.lobby.topplayers.location.other", playerLocation);		
		this.plugin.saveConfig();
		
	}
	
	//setTopPlayer Location
	public final void setShopPosition(final Location playerLocation) {
		
		this.configuration.set("game.lobby.shop.location", playerLocation);
		this.plugin.saveConfig();
	}
	
	//gets the TopPlayer Location
	public final Map<Integer, Location> getArmorstandPositions(){
		Map<Integer, Location> map = Maps.newHashMap();

		map.put(1, this.configuration.getLocation("game.lobby.topplayers.location.1"));
		map.put(2, this.configuration.getLocation("game.lobby.topplayers.location.2"));
		map.put(3, this.configuration.getLocation("game.lobby.topplayers.location.3"));

		return map;
	}
	
	//gets the TopLower Player Location
	public final Location getTopLowerPositions(){
		return this.configuration.getLocation("game.lobby.topplayers.location.other");
	}
	
	
	//gets the TopPlayer Location
	public final Location getShopPosition() {
		
		if(!this.isShopExists()) {
			return null;
		}
		return this.configuration.getLocation("game.lobby.shop.location");
	}
}



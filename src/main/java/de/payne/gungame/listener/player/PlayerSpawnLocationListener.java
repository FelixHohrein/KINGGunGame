package de.payne.gungame.listener.player;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.payne.gungame.GunGame;


public class PlayerSpawnLocationListener implements Listener {
	
	//setzt die spawnlocation eines jeden spielers auf die currentmap location
	@EventHandler
	public final void handlePlayerSpawn(final PlayerSpawnLocationEvent event) {
		if(!GunGame.getInstance().getMapConfig().alreadyExists("Lobby")) {
			return;
		}
		event.setSpawnLocation(GunGame.getInstance().getLobbySpawn().getSpawnLocation());
	}

}

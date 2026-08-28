package de.payne.gungame.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import de.payne.gungame.GunGame;

public final class PlayerBucketEmptyListener implements Listener {

	
	 @EventHandler
	public final void playerbucketEmptyListener(final PlayerBucketEmptyEvent event) {
		 if(GunGame.getInstance().getBuilders().contains(event.getPlayer())) {
				event.setCancelled(false);
		 } else {
			event.setCancelled(true);
		 }		
	}
}

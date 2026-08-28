package de.payne.gungame.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import de.payne.gungame.GunGame;

public final class PlayerBucketFillListener implements Listener {

	@EventHandler
	public final void playerBucketFillListener(final PlayerBucketFillEvent event) {
		if(GunGame.getInstance().getBuilders().contains(event.getPlayer())) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}
}

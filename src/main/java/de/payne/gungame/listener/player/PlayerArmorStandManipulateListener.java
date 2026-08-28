package de.payne.gungame.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public final class PlayerArmorStandManipulateListener implements Listener {

	
	@EventHandler
	public final void playerArmorStandManipulateListener(final PlayerArmorStandManipulateEvent event) {
		event.setCancelled(true);
	}
}

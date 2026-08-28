package de.payne.gungame.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class HangingPlaceListener implements Listener {

	
	@EventHandler
	public void hangingPlaceListener(HangingPlaceEvent event) {
		event.setCancelled(true);
	}
}
